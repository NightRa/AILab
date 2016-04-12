namespace MDKnapsack

module Branch = 
    open System
    open System.Collections.Generic
    
    let dfs (endTime : DateTime) (items : Item array) (optimums : (Knapsack * int) array) (knownOpt : int) : Solution * DateTime option = 
        let startingSolution = Solution.Empty items
        let mutable bestSolution = startingSolution
        let mutable maybeFindingTime : DateTime option = None
        let solutionsToBranch = new Stack<Solution * int>()
        solutionsToBranch.Push(startingSolution, 0)
        while DateTime.Now < endTime && solutionsToBranch.Count > 0 do
            let sol, index = solutionsToBranch.Pop()
            let nextIndex = index + 1
            if nextIndex = items.Length then 
                if sol.Price > bestSolution.Price then 
                    bestSolution <- sol
                    if bestSolution.Price = knownOpt then maybeFindingTime <- Some(DateTime.Now)
            else 
                let with1, with0 = sol.Branch nextIndex
                if with0.IsValid optimums then solutionsToBranch.Push(with0, nextIndex)
                if with1.IsValid optimums then solutionsToBranch.Push(with1, nextIndex)
            ()
        (bestSolution, maybeFindingTime)
    
    let runAlg alg (problem : KnapsackProblem) (time : TimeSpan) (optimum : Knapsack -> int) : Solution * TimeSpan option = 
        let optimums = problem.Knapsacks |> Array.map (fun k -> (k, optimum k))
        let items = problem.Items
        let startingTime = DateTime.Now
        let endTime = startingTime + time
        let (sol, maybeTime) = alg endTime (items) optimums (problem.Optimal)
        match maybeTime with
        | None -> (sol, None)
        | Some findingTime -> (sol, Some <| findingTime - startingTime)
    
    let dfsSorted (problem : KnapsackProblem) = 
        let itemHeuristicValue (item : Item) = 
            problem.Knapsacks |> Array.averageBy (fun k -> 
                                     let price = float32 <| item.Price
                                     let cons = float32 <| item.ConstraintOf k
                                     price / cons)
        
        let sortedItems = problem.Items |> Array.sortByDescending (itemHeuristicValue)
        let newProblem = KnapsackProblem(problem.Name, sortedItems, problem.Knapsacks, problem.Optimal)
        runAlg dfs newProblem
    
    let bestFirst (endTime : DateTime) (items : Item array) (optimums : (Knapsack * int) array) (knownOpt : int) : Solution * DateTime option =
        let startingSolution = Solution.Empty items
        let mutable bestSolution = startingSolution
        let mutable maybeFindingTime : DateTime option = None
        let solutionsToBranch = new Priority_Queue.SimplePriorityQueue<Solution * int>()
        solutionsToBranch.Enqueue ((startingSolution, 0), 1.0 / float startingSolution.Price)
        while DateTime.Now < endTime && solutionsToBranch.Count > 0 do
            let sol, index = solutionsToBranch.Dequeue ()
            let nextIndex = index + 1
            if nextIndex = items.Length then 
                if sol.Price > bestSolution.Price then 
                    bestSolution <- sol
                    if bestSolution.Price = knownOpt then maybeFindingTime <- Some(DateTime.Now)
            else 
                let with1, with0 = sol.Branch nextIndex
                if with0.IsValid optimums then solutionsToBranch.Enqueue ((with0, nextIndex), 1.0 / float with0.Price)
                if with1.IsValid optimums then solutionsToBranch.Enqueue ((with1, nextIndex), 1.0 / float with1.Price)
            ()
        (bestSolution, maybeFindingTime)
