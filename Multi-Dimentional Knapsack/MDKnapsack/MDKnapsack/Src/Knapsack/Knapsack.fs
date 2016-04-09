namespace MDKapsack

[<Struct>]
[<CustomEquality>]
[<NoComparison>]
type Knapsack(name : string, capacity : int) =
    member x.Name = name
    member x.Capacity = capacity
    override x.GetHashCode () =
        name.GetHashCode ()
    override x.Equals obj = 
        match obj with
        |(:? Knapsack as knapsack) ->  knapsack.Name.Equals name
        | _ -> false
    override x.ToString () =
        sprintf "name: %s, capacity: %d" name capacity


