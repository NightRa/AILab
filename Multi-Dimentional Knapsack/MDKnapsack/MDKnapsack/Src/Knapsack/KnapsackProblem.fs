namespace MDKnapsack

open System
open MDKnapsack.Util
open System.Text

[<Class>]
[<Sealed>]
type KnapsackProblem(name : string, items : Item array, knapsacks : Knapsack array, optimal : int) = 
    
    member x.Name = 
        if not <| name.EndsWith " ok" then name
        else 
            name
            |> Seq.rev
            |> Seq.skip 3
            |> Seq.toArray
            |> string
    
    member x.Items = items
    member x.Knapsacks = knapsacks
    member x.Optimal = optimal
    
    override x.ToString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Name: " + x.Name) |> ignore
        strBuilder.AppendLine("optimal: " + optimal.ToString()) |> ignore
        strBuilder.AppendLine("knapsacks: ") |> ignore
        for knapsack in knapsacks do
            strBuilder.AppendLine("\t" + knapsack.ToString()) |> ignore
        strBuilder.AppendLine("items: ") |> ignore
        for item in items do
            strBuilder.AppendLine("\t" + item.ToString()) |> ignore
        strBuilder.ToString()
    
    member x.AsString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Name: " + name) |> ignore
        strBuilder.AppendLine("optimal: " + optimal.ToString()) |> ignore
        strBuilder.AppendLine("knapsacks: ") |> ignore
        for knapsack in knapsacks do
            strBuilder.AppendLine("\t" + knapsack.ToString()) |> ignore
        strBuilder.ToString()
    
    static member Create(nameOfGame : string, numOfKnapsacks : int, numOfItems : int, capacities : int [], 
                         prices : int [], constraints : int [] [], optimum : int) : KnapsackProblem = 
        assert (numOfKnapsacks = capacities.Length)
        let knapsacks = 
            seq { 1..numOfKnapsacks }
            |> Seq.map (fun i -> Knapsack(capacities.[i - 1]))
            |> Seq.toArray
        assert (numOfItems = prices.Length)
        assert (numOfKnapsacks = constraints.Length)
        let constraints = MDKnapsack.Util.Array.inverse constraints
        assert (constraints.Length = numOfItems)
        let items = 
            constraints
            |> Array.toSeq
            |> Seq.mapi (fun i c -> Item(prices.[i], Dictionary.from knapsacks c))
            |> Seq.toArray
        KnapsackProblem(nameOfGame, items, knapsacks, optimum)
    
    member x.AsInput() = 
        [ knapsacks.Length.ToString() ] 
        @ [ items.Length.ToString() ] 
          @ (items
             |> Array.map (fun i -> i.Price.ToString())
             |> Array.toList)
            @ (knapsacks
               |> Array.map (fun k -> k.Capacity.ToString())
               |> Array.toList)
              @ ((knapsacks |> Array.collect (fun k -> items |> Array.map (fun i -> (i.ConstraintOf k).ToString()))) 
                 |> Array.toList) @ [ optimal.ToString() ] @ [ name ]
