namespace MDKnapsack

module Branch = 
    open System
    open System.Collections.Generic
    
    let rec private solve (endTime : DateTime) (items : (int * Item) list) (optimums : (Knapsack * int) array) 
            (knownOpt : int) (currentSolution : Solution) (bestSolution : Solution) : (Solution * DateTime option) = 
        if DateTime.Now > endTime then 
            (bestSolution, None)
        else 
            match items with
            | [] -> (bestSolution, None)
            | ((index, item) :: restItems) -> 
                let soultionWithItem = currentSolution.With index
                let soultionWithoutItem = currentSolution.Without index
                let sols = new Stack<_> (2)
                for sol in [|soultionWithItem; soultionWithoutItem|] do
                    if sol.IsValid optimums then
                        let newBest = 
                           if sol.Price > bestSolution.Price then sol
                           else bestSolution
                        sols.Push <| solve endTime restItems optimums knownOpt sol newBest

                let (one, maybeOneTime) = sols.Pop ()
                if sols.Count = 0 then
                    (one, maybeOneTime)
                else
                    let (two, maybeTwoTime) = sols.Pop ()
                    if maybeOneTime.IsSome then
                        (one, maybeOneTime)
                    elif maybeTwoTime.IsSome then
                        (two, maybeTwoTime)
                    elif one.Price > two.Price then
                        if (one.Price = knownOpt) then
                            (one, Some DateTime.Now)
                        else
                            (one, None)
                    else
                        if (two.Price = knownOpt) then
                            (two, Some DateTime.Now)
                        else
                            (two, None)
    
    let dfs (time : TimeSpan) (optimum : Knapsack -> int) (problem : KnapsackProblem) : (Solution * TimeSpan option) = 
        let optimums = problem.Knapsacks |> Array.map (fun k -> (k, optimum k))
        let items = problem.Items |> Array.toList
        let startingSolution = Solution.Empty(problem.Items)
        let startingTime = DateTime.Now
        let endTime = startingTime + time
        let (sol, maybeTime) = solve endTime (items |> List.mapi MDKapsack.Util.General.asPair) optimums (problem.Optimal) startingSolution startingSolution
        match maybeTime with
        | None -> (sol, None)
        | Some findingTime -> (sol, Some <| findingTime - startingTime)
