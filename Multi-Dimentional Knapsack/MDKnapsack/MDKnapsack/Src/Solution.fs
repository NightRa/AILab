namespace MDKapsack

open System.Text

type Solution(items : Map<Item, IsTaking>) = 
    
    static member Empty(items : Item array) = 
        items
        |> Array.fold (fun map item -> Map.add item (IsTaking.No) map) Map.empty
        |> Solution
    
    member x.Takings = 
        items
        |> Map.toSeq
        |> Seq.filter (fun (item, isTaking) -> isYes isTaking)
        |> Seq.map fst
    
    member x.Price = x.Takings |> Seq.sumBy (fun item -> item.Price)
    
    member x.With item = 
        match items.[item] with
        | IsTaking.No -> 
            items
            |> Map.add item (IsTaking.Yes)
            |> Solution
        | IsTaking.Yes -> x
        | _ -> failwith "incomplete pattern"
    
    member x.Without item = 
        match items.[item] with
        | IsTaking.Yes -> 
            items
            |> Map.add item (IsTaking.No)
            |> Solution
        | IsTaking.No -> x
        | _ -> failwith "incomplete pattern"
    
    member x.IsValid(optimums : (Knapsack * int) array) = 
        optimums
        |> Seq.ofArray
        |> Seq.forall (fun (k, max) -> 
               let sumOfConstraints = x.Takings |> Seq.sumBy (fun i -> i.ConstraintOf k)
               sumOfConstraints < k.Capacity && sumOfConstraints < max)
    
    override x.ToString() = 
        let strBuilder = new StringBuilder()
        strBuilder.AppendLine("Price: " + x.Price.ToString()) |> ignore
        items
        |> Map.toSeq
        |> Seq.map 
               (fun (item, isTaking) -> 
               "item " + item.Name + ", is taking: " + (asString isTaking) + ", price: " + item.Price.ToString())
        |> Seq.map strBuilder.AppendLine
        |> Seq.iter ignore
        strBuilder.ToString()
