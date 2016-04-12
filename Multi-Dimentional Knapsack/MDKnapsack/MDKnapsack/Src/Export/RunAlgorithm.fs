namespace MDKnapsack

module RunAlgorithm =
    open Branch
    open System.IO
    open Parsing
    open MDKnapsack
    
    let public runAlgorithm (parameters : AlgParameters) =
        let alg = match parameters.Alg with
                    | Alg.BestFirst -> runAlg bestFirst
                    | Alg.DfsNotSorted -> runAlg dfs
                    | Alg.DfsSorted -> dfsSorted
                    | _ -> failwith "Incomplete pattern"
        let optimumFunc =  match parameters.BoundKnapsack with
                            | BoundKnapsack.Unbounded -> Bound.unboundedKnapsack
                            | BoundKnapsack.Fractional -> Bound.fractionedFilledKnapsack
                            | _ -> failwith "Incomplete pattern"
                        
        let time = parameters.AlgTime

        let problems = parameters.DatFilePathes
                        |> Seq.ofArray
                        |> Seq.map (fun f -> (f, File.ReadAllText f))
                        |> Seq.map (fun (f, str) -> (f, parseToKnapsackProblem str))
                        |> Seq.toArray

        seq {
            for (filename, prob) in problems ->
                let solution, maybeTime = alg prob time (optimumFunc prob.Items)
                in (filename, prob, solution,  maybeTime)
        }

