namespace MDKnapsack.Main

module Program = 
    open System.IO
    open MDKnapsack.Parsing
    open MDKnapsack.Branch
    open System
    open MDKnapsack
    open System.Collections.Generic
    
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
        
        let solution = knapsack |> dfs time (optimumFunc knapsack.Items)
        (solution, knapsack)
    
    let testingProblem () =
        let k = Knapsack (100)
        let c1 = new Dictionary<_,_>()
        c1.Add (k, 20)
        let item1 = new Item (100, c1)
        let c2 = new Dictionary<_,_>()
        c2.Add (k, 90)
        let item2 = new Item (200, c2)
        let items = [|item1; item2|]
        (k, items, Bound.fractionedFilledKnapsack items k)

    [<EntryPoint>]
    let main argv = 
        //printfn "%A" <| testingProblem ()
        testSolution time fractionalOptimumFunc |> fun ((solution, maybeTime), knapsack) ->
            match maybeTime with
            | None -> "Didnt find best solution"
            | Some time -> "Found best in " + time.TotalSeconds.ToString() + "secs"
            |> printfn "%s" 
            printfn "solution:"
            printfn "%A" solution
            printfn ""
            printfn "knapsack: "
            printfn "%A" knapsack
        0 // return an integer exit code
