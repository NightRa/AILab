namespace MDKapsack.Main

module Program =
    open System.IO
    open MDKapsack.Parsing
    

    let testParsing () =
        let filename = @"C:\Users\yuval\Desktop\AILab\AILab\Multi-Dimentional Knapsack\samples\PB2.DAT"
        filename
        |> File.ReadAllText
        |> parseToKnapsackProblem
        
    let testParsingInput () =
        testParsing ()
        |> fun k -> k.AsInput ()
        |> MDKapsack.Util.List.mkString " "

    [<EntryPoint>]
    let main argv = 
        printfn "Hey!"
        0 // return an integer exit code

