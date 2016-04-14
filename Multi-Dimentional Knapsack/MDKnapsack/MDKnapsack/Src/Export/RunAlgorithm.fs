namespace MDKnapsack

module RunAlgorithm = 
    open Branch
    open System.IO
    open Parsing
    open MDKnapsack
    open System
    open ChartingUtil
    open System.Text
    open MDKnapsack.Util
    open Bound
    
    let getPruneFunc = 
        function 
        | BoundKnapsack.Unbounded -> Bound.unboundedKnapsack
        | BoundKnapsack.Fractional -> Bound.upperBoundFractional
        | _ -> failwith "Incomplete pattern"
    
    let getAlg (prunning : PrunningFunc) = 
        function 
        | Alg.BestFirst -> runAlg bestFirst prunning
        | Alg.DfsNotSorted -> runAlg dfs prunning
        | Alg.DfsSorted -> dfsSorted prunning
        | _ -> failwith "Incomplete pattern"
    
    let getProb (parameters : AlgParameters, datFile : string) = 
        try 
            assert (File.Exists datFile)
            let prunning = getPruneFunc (parameters.BoundKnapsack)
            let alg = getAlg prunning parameters.Alg
            let time = parameters.AlgTime
            let filename = datFile
            let text = File.ReadAllText filename
            let prob = parseToKnapsackProblem text
            (prob, alg, time)
        with _ -> raise <| Exception("Error in file: " + datFile)
    
    let public runAlgorithmSingle (parameters : AlgParameters, datFile : string, respond : Action) = 
        respond.Invoke()
        let prob, alg, time = getProb (parameters, datFile)
        let solution, maybeTime = alg prob time
        respond.Invoke()
        (prob, solution, maybeTime, parameters)
    
    let public runAlgorithmOnParams (parameters : AlgParameters [], datFile : string, respond : Action) = 
        let name = ref "No Name"
        parameters
        |> Array.map (fun p -> runAlgorithmSingle (p, datFile, respond))
        |> Array.map (fun (prob, sol, _, par) -> 
               name := prob.Name
               (par.AsString(), float sol.Price / float prob.Optimal))
        |> Array.unzip
        |> asSingleChartColumn (!name)
    
    let public runAlgorithmMultipleDats (parameters : AlgParameters, datFiles : string [], respond : Action) = 
        let name = ref "No Name"
        datFiles
        |> Array.map (fun d -> 
               respond.Invoke()
               runAlgorithmSingle (parameters, d, respond))
        |> Array.map (fun (prob, sol, _, par) -> 
               name := par.AsString()
               (prob.Name, float sol.Price / float prob.Optimal))
        |> Array.unzip
        |> asSingleChartColumn (!name)
    
    let public asString (x : Tuple<KnapsackProblem * Solution * TimeSpan option * AlgParameters>) : string [] = 
        let (prob, sol, maybeTime, algParams) = x.Item1
        let strBuilder = new StringBuilder()
        let strBuilder = strBuilder.AppendLine <| "Problem name: " + (prob.Name)
        let strBuilder = strBuilder.AppendLine <| "Optimal: " + (prob.Optimal.ToString())
        
        let strBuilder = 
            strBuilder.AppendLine <| match maybeTime with
                                     | None -> "Didnt find optimal, found: " + (sol.Price.ToString())
                                     | Some t -> "Found optimal in " + (t.TotalSeconds.ToString()) + "sec"
        
        let strBuilder = strBuilder.AppendLine <| "Parameters: " + (algParams.AsString().FillLinesWithTabs())
        let strBuilder = strBuilder.AppendLine <| "Problem: " + (prob.AsString().FillLinesWithTabs())
        let strBuilder = strBuilder.AppendLine <| "Solution: " + (sol.ToString().FillLinesWithTabs())
        strBuilder.ToString().SplitToLines()
