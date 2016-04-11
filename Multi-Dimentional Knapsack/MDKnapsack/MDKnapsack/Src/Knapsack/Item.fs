namespace MDKnapsack

open System.Collections.Generic
open System
open System.Text

[<Class>]
[<Sealed>]
type Item(price : int, constraints : Dictionary<Knapsack, int>) = 
    member x.Price = price
    member x.ConstraintOf(knapsack : Knapsack) = constraints.[knapsack]
    override x.ToString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Item: price = " + price.ToString() + " ") |> ignore
        for knapsack in constraints.Keys do
            strBuilder.AppendLine("\t\t" + (constraints.[knapsack].ToString()) + "/ " + knapsack.Capacity.ToString()) 
            |> ignore
        strBuilder.ToString()
