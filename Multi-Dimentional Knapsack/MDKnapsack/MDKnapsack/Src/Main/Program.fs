namespace MDKnapsack.Main

module Program = 
    open System.IO
    open MDKnapsack.Parsing
    open MDKnapsack.Branch
    open System
    open MDKnapsack
    open System.Collections.Generic
    open FSharp.Charting
    
    let time = TimeSpan.FromSeconds 5.0
    let unboundedOptimumFunc = Bound.unboundedKnapsack
    let fractionalOptimumFunc = Bound.fractionedFilledKnapsack
    let filename = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\FLEI.DAT"
    
    let testParsing() = 
        filename
        |> File.ReadAllText
        |> parseToKnapsackProblem
    
    let testParsingInput() = 
        testParsing()
        |> fun k -> k.AsInput()
        |> Util.List.mkString " "
    
    let testSolution time optimumFunc = 
        let knapsack = 
            filename
            |> File.ReadAllText
            |> parseToKnapsackProblem
        
        let (solution, maybeTime) = runAlg bestFirst knapsack time (optimumFunc knapsack.Items)
        (solution, maybeTime, knapsack)
    
    let testingProblem() = 
        let k = Knapsack(100)
        let c1 = new Dictionary<_, _>()
        c1.Add(k, 20)
        let item1 = new Item(100, c1)
        let c2 = new Dictionary<_, _>()
        c2.Add(k, 90)
        let item2 = new Item(200, c2)
        let items = [| item1; item2 |]
        (k, items, Bound.fractionedFilledKnapsack items k)
    
    [<EntryPoint>]
    let main argv = 
        let time = TimeSpan.FromSeconds 1.0
        let file1 = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\FLEI.DAT"
        let file2 = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\WEISH04.DAT"
        let file3 = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\WEISH07.DAT"
        //let data : IObservable<seq<int, int>> = Observable ()
        //let chart = FSharp.Charting.LiveChart.Point ()
        //LiveChart.
        let files = [|file1; file2; file3|]
        for alg in [Alg.BestFirst; Alg.DfsNotSorted; Alg.DfsSorted] do
            for bounded in [BoundKnapsack.Fractional; BoundKnapsack.Unbounded] do
                let parameters = new AlgParameters(alg, bounded, files, time)
                for filename, prob, solution, maybeTime in RunAlgorithm.runAlgorithm (parameters) do
                    printfn "%s" <| "Filename: " + (Path.GetFileName filename)
                    printfn "%s" <| "Problem name: " + (prob.Name)
                    printfn "%s" <| "Optimal: " + (prob.Optimal.ToString ())
                    printfn "%s" <| match maybeTime with
                                    | None -> "Didnt find optimal, found: " + (solution.Price.ToString ())
                                    | Some t -> "Found optimal in " + (t.TotalSeconds.ToString()) + "sec"
                    printfn "%s" <| "Alg: " + (Alg.asString alg)
                    printfn "%s" <| "Bound: " + (BoundKnapsack.asString bounded)
                    printfn "****************************"
            

        0 // return an integer exit code
