namespace MDKnapsack

module Parsing = 
    open System
    open ParsingUtil
    
    let parseToKnapsackProblem (data : string) : KnapsackProblem = 
        let dataStream = 
            data
            |> String.map (fun ch -> 
                   if Char.IsDigit ch then ch
                   elif Char.IsLetter ch then ch
                   else ' ')
            |> fun str -> str.Split([| ' ' |], StringSplitOptions.RemoveEmptyEntries)
            |> Array.map (toStringOrInt)
            |> Array.toList
        
        let (numOfKnapsacks, dataStream) = getInt dataStream
        let (numOfObjects, dataStream) = getInt dataStream
        let (prices, dataStream) = getInts1 numOfObjects dataStream
        let (capacities, dataStream) = getInts1 numOfKnapsacks dataStream
        let (constraints, dataStream) = getInts2 numOfKnapsacks numOfObjects dataStream
        let (optimum, dataStream) = getInt dataStream
        let nameOfGame = getRest dataStream
        KnapsackProblem.Create(nameOfGame, numOfKnapsacks, numOfObjects, capacities, prices, constraints, optimum)
