namespace MDKapsack

module Branch = 
    open System
    
    let rec private solve (endTime : DateTime) (items : Item list) (optimums : (Knapsack * int) array) 
            (currentSolution : Solution) (bestSolution : Solution) : Solution = 
        if DateTime.Now > endTime then bestSolution
        else 
            match items with
            | [] -> bestSolution
            | (item :: restItems) -> 
                let soultionWithItem = currentSolution.With item
                let soultionWithoutItem = currentSolution.Without item
                [ soultionWithItem; soultionWithoutItem ]
                |> Seq.ofList
                |> Seq.filter (fun sol -> sol.IsValid optimums)
                |> Seq.map (fun sol -> 
                       let newBest = 
                           if sol.Price > bestSolution.Price then sol
                           else bestSolution
                       solve endTime restItems optimums sol newBest)
                |> Seq.maxBy (fun s -> s.Price)
    
    let dfs (time : TimeSpan) (optimum : Knapsack -> Item array -> int) (problem : KnapsackProblem) : Solution = 
        let optimums = problem.Knapsacks |> Array.map (fun k -> (k, optimum k problem.Items))
        let endTime = DateTime.Now + time
        let items = problem.Items |> Array.toList
        let startingSolution = Solution.Empty(problem.Items)
        solve endTime items optimums startingSolution startingSolution


