namespace MDKnapsack

module ParsingUtil = 
    open System

    type StringOrInt = 
        | Integer of int
        | String of string
    
    let toStringOrInt str = 
        let res = ref 0
        match Int32.TryParse(str, res) with
        | true -> Integer(!res)
        | false -> String(str)
    
    let getInt (tokens : StringOrInt list) : int * StringOrInt list = 
        match tokens with
        | [] -> failwith "get int on End of data"
        | (Integer i) :: rest -> (i, rest)
        | (String str) :: rest -> failwith ("get int on string: " + str)
    
    let getInts1 num tokens = 
        let numList = new System.Collections.Generic.List<int>()
        let mutable newTokens = tokens
        for i = 1 to num do
            let (a, b) = getInt newTokens
            newTokens <- b
            numList.Add a
        (numList.ToArray(), newTokens)
    
    let getInts2 num1 num2 tokens : int [] [] * StringOrInt list = 
        let numList = new System.Collections.Generic.List<int array>()
        let mutable newTokens = tokens
        for i = 1 to num1 do
            let (a, b) = getInts1 num2 newTokens
            newTokens <- b
            numList.Add a
        (numList.ToArray(), newTokens)
    
    let rec getRest (dataStream : StringOrInt list) : string = 
        match dataStream with
        | [] -> ""
        | (Integer i) :: xs -> i.ToString() + " " + (getRest xs)
        | (String s) :: xs -> s + " " + (getRest xs)
