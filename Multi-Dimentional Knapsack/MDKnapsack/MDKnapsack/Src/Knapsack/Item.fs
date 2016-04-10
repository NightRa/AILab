namespace MDKapsack

open System.Collections.Generic
open System

[<Struct>]
[<CustomEquality>]
[<CustomComparison>]
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
    interface IComparable<Item> with
        member x.CompareTo other = name.CompareTo (other.Name)
    interface IComparable with
        member x.CompareTo obj = 
            match box obj with
            |(:? Item as item) -> (x :> IComparable<Item>).CompareTo item
            | _ -> failwith "Unauthorized comparison"