namespace MDKapsack

open System.Collections.Generic
open System

[<Struct>]
[<CustomEquality>]
[<NoComparison>]
type Item(name : string, price : int, constraints : Dictionary<Knapsack, int>) = 
    member x.Name = name
    member x.Price = price
    
    member x.ConstraintOf(knapsack : Knapsack) = 
        if constraints.ContainsKey knapsack then constraints.[knapsack]
        else failwith <| sprintf "knapsack wasnt found :(. constraints: %A" constraints
    
    override x.GetHashCode() = name.GetHashCode()
    override x.Equals obj = 
        match obj with
        | (:? Item as item) -> item.Name.Equals name
        | _ -> false
    override x.ToString () =
        (sprintf "Name: %s" name) + ", " +
        (sprintf "price: %A" price) + ", " +
        (sprintf "constraints: %A" (constraints |> Seq.toList))
        

type IsTaking = 
    | No = 0
    | Yes = 1

type ItemTaking = Item -> IsTaking

[<CompilationRepresentation(CompilationRepresentationFlags.ModuleSuffix)>]
module ItemTaking = 
    let takings (items : Item array) (isTaking : Item -> IsTaking) : Item seq = 
        items
        |> Array.toSeq
        |> Seq.filter (fun item -> 
               match isTaking item with
               | IsTaking.Yes -> true
               | IsTaking.No -> false)
    
    let totalWeights (items : Item array) (isTaking : Item -> IsTaking) : Knapsack -> int = 
        let takenItems = takings items isTaking
        fun knapsack -> takenItems |> Seq.sumBy (fun item -> item.ConstraintOf knapsack)
    
    let isValid (knapsacks : Knapsack array) (items : Item array) (isTaking : Item -> IsTaking) : bool = 
        let getWeightOfKnapsack = totalWeights items isTaking
        knapsacks |> Array.forall (fun k -> getWeightOfKnapsack k < k.Capacity)
