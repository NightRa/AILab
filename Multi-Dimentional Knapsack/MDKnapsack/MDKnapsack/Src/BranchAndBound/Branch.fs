namespace MDKnapsack

module Branch = 
    open System
    open System.Collections.Generic
    open System.Diagnostics
    open Bound

    let dfs upperBoundFunc (endTime : DateTime) (prob : KnapsackProblem) : Solution * DateTime option = 
        let startingSolution = Solution.Empty prob
        let mutable bestSolution = startingSolution
        let mutable maybeFindingTime : DateTime option = None
        let solutionsToBranch = new Stack<Solution>()
        solutionsToBranch.Push (startingSolution)
        while DateTime.Now < endTime && solutionsToBranch.Count > 0 do
            let sol = solutionsToBranch.Pop()
            let openedBits = sol.OpenBits
            let nextIndex = sol.OpenBits
            if sol.Price > bestSolution.Price then 
                bestSolution <- sol
                if bestSolution.Price = sol.Prob.Optimal then maybeFindingTime <- Some(DateTime.Now)
            if openedBits < sol.Prob.Items.Length then
                let with1, with0 = sol.Branch
                if with0.IsValid && not <| with0.ShouldPrune (bestSolution.Price, upperBoundFunc) then 
                    solutionsToBranch.Push with0
                if with1.IsValid && not <| with1.ShouldPrune (bestSolution.Price, upperBoundFunc) then 
                    solutionsToBranch.Push with1
            ()
        (bestSolution, maybeFindingTime)
    
    let runAlg alg upperBoundFunc (prob : KnapsackProblem) (time : TimeSpan) : Solution * TimeSpan option = 
        let startingTime = DateTime.Now
        let endTime = startingTime + time
        let (sol, maybeTime) = alg upperBoundFunc endTime prob
        let timeToFind = maybeTime |> Option.map (fun findingTime -> findingTime - startingTime)
        (sol, timeToFind)
    
    
    let bestFirst upperBoundFunc (endTime : DateTime) (prob : KnapsackProblem) : Solution * DateTime option = 
        let priority s = 1.0 / (float <| partialDensity s)
        let startingSolution = Solution.Empty prob
        let mutable bestSolution = startingSolution
        let mutable maybeFindingTime : DateTime option = None
        let solutionsToBranch = new Priority_Queue.SimplePriorityQueue<Solution>()
        solutionsToBranch.Enqueue(startingSolution, priority startingSolution)
        while DateTime.Now < endTime && solutionsToBranch.Count > 0 do
            let sol = solutionsToBranch.Dequeue()
            let openedBits = sol.OpenBits
            let nextIndex = openedBits
            if sol.Price > bestSolution.Price then 
                bestSolution <- sol
                if bestSolution.Price = sol.Prob.Optimal then maybeFindingTime <- Some(DateTime.Now)
            if openedBits < sol.Prob.Items.Length then
                let with1, with0 = sol.Branch
                if with0.IsValid && not <| with0.ShouldPrune (bestSolution.Price, upperBoundFunc) then 
                    solutionsToBranch.Enqueue(with0, priority with0)
                if with1.IsValid && not <| with1.ShouldPrune (bestSolution.Price, upperBoundFunc) then 
                    solutionsToBranch.Enqueue(with1, priority with1)
            ()
        (bestSolution, maybeFindingTime)
        
    let runSorted alg upperBoundFunc (problem : KnapsackProblem) = 
        let itemHeuristicValue (item : Item) = 
            let avgConstraint = 
                problem.Knapsacks |> Array.averageBy (fun k -> 
                                         let cons = double <| item.ConstraintOf k
                                         let capacity = double <| k.Capacity
                                         cons / capacity)
            (double item.Price) / avgConstraint
        
        let sortedItems = problem.Items |> Array.sortByDescending (itemHeuristicValue)
        let newProblem = KnapsackProblem(problem.Name, sortedItems, problem.Knapsacks, problem.Optimal)
        runAlg alg upperBoundFunc newProblem


        