namespace MDKapsack

open System
open MDKapsack.Util

type KnapsackProblem(name : string, items : Item array, knapsacks : Knapsack array, optimal : int) = 
    member x.Name = name
    member x.Items = items
    member x.Knapsacks = knapsacks
    member x.Optimal = optimal
    
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
                 |> Array.toList)
                @ [optimal.ToString ()]
                    @ [name]
    
    override x.ToString() = 
        (sprintf "Name: %s" name) + Environment.NewLine + (sprintf "items: %A" items) + Environment.NewLine 
        + (sprintf "knapsacks: %A" knapsacks) + Environment.NewLine + (sprintf "optimal: %d" optimal)
    static member Create(nameOfGame : string, numOfKnapsacks : int, numOfItems : int, capacities : int [], 
                         prices : int [], constraints : int [] [], optimum : int) : KnapsackProblem = 
        assert (numOfKnapsacks = capacities.Length)
        let knapsacks = 
            seq { 1..numOfKnapsacks }
            |> Seq.map (fun i -> Knapsack("Knasack" + i.ToString(), capacities.[i - 1]))
            |> Seq.toArray
        assert (numOfItems = prices.Length)
        assert (numOfKnapsacks = constraints.Length)
        let constraints = Array.inverse constraints
        assert (constraints.Length = numOfItems)
        let items = 
            constraints
            |> Array.toSeq
            |> Seq.mapi (fun i c -> Item("Item" + i.ToString(), prices.[i], Dictionary.from knapsacks c))
            |> Seq.toArray
        KnapsackProblem(nameOfGame, items, knapsacks, optimum)
