namespace MDKapsack.Main

module Program =
    open System.IO
    open MDKapsack.Parsing
    open MDKapsack.Branch
    open System
    open MDKapsack
    
    let time = TimeSpan.FromSeconds 5.0
    let optimumFunc = Bound.unboundedKnapsack
    let filename = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\FLEI.DAT"

    let testParsing () =
        filename
        |> File.ReadAllText
        |> parseToKnapsackProblem
        
    let testParsingInput () =
        testParsing ()
        |> fun k -> k.AsInput ()
        |> MDKapsack.Util.List.mkString " "

    let testSolution time optimumFunc =
        let knapsack = filename
                        |> File.ReadAllText
                        |> parseToKnapsackProblem

        let solution = knapsack
                        |> dfs time optimumFunc

        (solution, knapsack)

    [<EntryPoint>]
    let main argv = 
        testSolution time optimumFunc
        |> fun (solution, knapsack) ->  printfn "solution:"
                                        printfn "%A" solution
                                        printfn ""
                                        printfn "knapsack: "
                                        printfn "%A" knapsack

        Console.ReadKey () |> ignore
        0 // return an integer exit code

