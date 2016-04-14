namespace MDKnapsack

module Branch = 
    open System
    open System.Collections.Generic
    open System.Diagnostics
    open Bound

    let dfs (prunning : PrunningFunc) (endTime : DateTime) (items : Item array) (knapsacks : Knapsack array) (knownOpt : int) : Solution * DateTime option = 
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
                if with0.IsValid knapsacks && not <| with0.ShouldPrune(nextIndex, bestSolution.Price, prunning, knapsacks) then 
                    solutionsToBranch.Push(with0, nextIndex)
                if with1.IsValid knapsacks && not <| with1.ShouldPrune(nextIndex, bestSolution.Price, prunning, knapsacks) then 
                    solutionsToBranch.Push(with1, nextIndex)
            ()
        (bestSolution, maybeFindingTime)
    
    let runAlg alg (prunning : PrunningFunc) (problem : KnapsackProblem) (time : TimeSpan) : Solution * TimeSpan option = 
        let items = problem.Items
        let startingTime = DateTime.Now
        let endTime = startingTime + time
        let (sol, maybeTime) = alg prunning endTime (items) (problem.Knapsacks) (problem.Optimal)
        let timeToFind = maybeTime |> Option.map (fun findingTime -> findingTime - startingTime)
        (sol, timeToFind)
    
    let dfsSorted (prunning : PrunningFunc) (problem : KnapsackProblem) = 
        let itemHeuristicValue (item : Item) = 
            let avgConstraint = 
                problem.Knapsacks |> Array.averageBy (fun k -> 
                                         let cons = double <| item.ConstraintOf k
                                         let capacity = double <| k.Capacity
                                         1.0 - cons / capacity)
            (double item.Price) * avgConstraint
        
        let sortedItems = problem.Items |> Array.sortByDescending (itemHeuristicValue)
        let newProblem = KnapsackProblem(problem.Name, sortedItems, problem.Knapsacks, problem.Optimal)
        runAlg dfs prunning newProblem
    
    let bestFirst (prunning : PrunningFunc) (endTime : DateTime) (items : Item array) (knapsacks : Knapsack array) (knownOpt : int) : Solution * DateTime option = 
        let startingSolution = Solution.Empty items
        let mutable bestSolution = startingSolution
        let mutable maybeFindingTime : DateTime option = None
        let solutionsToBranch = new Priority_Queue.SimplePriorityQueue<Solution * int>()
        solutionsToBranch.Enqueue((startingSolution, 0), 1.0 / float startingSolution.Price)
        while DateTime.Now < endTime && solutionsToBranch.Count > 0 do
            let sol, index = solutionsToBranch.Dequeue()
            let nextIndex = index + 1
            if nextIndex = items.Length then 
                if sol.Price > bestSolution.Price then 
                    bestSolution <- sol
                    if bestSolution.Price = knownOpt then maybeFindingTime <- Some(DateTime.Now)
            else 
                let with1, with0 = sol.Branch nextIndex
                if with0.IsValid knapsacks && not <| with0.ShouldPrune(nextIndex, bestSolution.Price, prunning, knapsacks) then 
                    solutionsToBranch.Enqueue((with0, nextIndex), 1.0 / float with0.Price)
                if with1.IsValid knapsacks && not <| with1.ShouldPrune(nextIndex, bestSolution.Price, prunning, knapsacks) then 
                    solutionsToBranch.Enqueue((with1, nextIndex), 1.0 / float with1.Price)
            ()
        (bestSolution, maybeFindingTime)
