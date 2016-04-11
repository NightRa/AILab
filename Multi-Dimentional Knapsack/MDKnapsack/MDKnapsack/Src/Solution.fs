namespace MDKnapsack

open System.Text
open System.Collections

type Solution(itemsTaken : BitArray, items : Item array) = 
    static member Empty(items : Item array) = (new BitArray(items.Length, false), items) |> Solution
    
    member x.Price = 
        let mutable sum = 0
        for i = 0 to items.Length - 1 do
            if itemsTaken.[i] then sum <- sum + items.[i].Price
        sum
    
    member this.With itemIndex = 
        if itemsTaken.[itemIndex] then this
        else 
            let newBitArray = itemsTaken.Clone() :?> BitArray
            newBitArray.[itemIndex] <- true
            Solution(newBitArray, items)
    
    member this.Without itemIndex = 
        if not itemsTaken.[itemIndex] then this
        else 
            let newBitArray = itemsTaken.Clone() :?> BitArray
            newBitArray.[itemIndex] <- false
            Solution(newBitArray, items)
    
    member x.IsValid(optimums : (Knapsack * int) array) = 
        optimums |> Array.forall (fun (knapsack, maxBound) -> 
                        let mutable sumOfConstraints = 0
                        for i = 0 to items.Length - 1 do
                            if itemsTaken.[i] then 
                                sumOfConstraints <- sumOfConstraints + items.[i].ConstraintOf knapsack
                        sumOfConstraints < knapsack.Capacity && sumOfConstraints < maxBound)
    
    override x.ToString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Price: " + x.Price.ToString()) |> ignore
        seq { 0..itemsTaken.Length - 1 }
        |> Seq.map (fun i -> (i, itemsTaken.[i]))
        |> Seq.sortByDescending snd
        |> Seq.map (fun (i, isTaken) -> 
               match isTaken with
               | true -> "\tTaken: " + items.[i].Price.ToString()
               | false -> "\tNot Taken: " + items.[i].Price.ToString())
        |> Seq.map (strBuilder.AppendLine)
        |> Seq.iter ignore
        strBuilder.ToString()
