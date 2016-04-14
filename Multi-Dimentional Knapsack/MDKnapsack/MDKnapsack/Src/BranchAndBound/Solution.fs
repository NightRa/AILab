namespace MDKnapsack

open System.Text
open System.Collections
open MDKnapsack.Util
open Bound

type Solution(itemsTaken : BitArray, items : Item array) = 

    let price = 
        let mutable sum = 0
        for i = 0 to items.Length - 1 do
            if itemsTaken.[i] then sum <- sum + items.[i].Price
        sum
    
    member x.Price = price
    static member Empty(items : Item array) = (new BitArray(items.Length, false), items) |> Solution
    
    member x.IsValid (knapsacks : Knapsack array) = 
        knapsacks |> Array.forall (fun knapsack -> 
                        let mutable sumOfConstraints = 0
                        for i = 0 to items.Length - 1 do
                            if itemsTaken.[i] then 
                                sumOfConstraints <- sumOfConstraints + items.[i].ConstraintOf knapsack
                        sumOfConstraints < knapsack.Capacity)

    member x.ShouldPrune (doneUntilIndex : int, bestPrice : int, prunning : PrunningFunc, knapsacks : Knapsack[]) : bool =
        let mutable restPotentialPrice = 0
        for i = doneUntilIndex + 1 to itemsTaken.Length - 1 do
            restPotentialPrice <- restPotentialPrice + items.[i].Price
        let maxPossible = price + restPotentialPrice
        maxPossible < bestPrice || prunning knapsacks price items doneUntilIndex < bestPrice
    
    member x.Branch (index : int) =
        let newCopy = itemsTaken.Clone () :?> BitArray
        if (itemsTaken.[index]) then
            newCopy.[index] <- false
            (x, Solution (newCopy, items))
        else
            newCopy.[index] <- true
            (Solution (newCopy, items), x)


    override x.ToString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Price: " + x.Price.ToString()) |> ignore
        seq { 0..itemsTaken.Length - 1 }
        |> Seq.map (fun i -> (i, itemsTaken.[i]))
        |> Seq.sortByDescending snd
        |> Seq.map (fun (i, isTaken) -> 
               match isTaken with
               | true -> "\tTaken: " + items.[i].ToString().FillLinesWithTabs()
               | false -> "\tNot Taken: " + items.[i].ToString())
        |> Seq.map (strBuilder.AppendLine)
        |> Seq.iter ignore
        strBuilder.ToString()
