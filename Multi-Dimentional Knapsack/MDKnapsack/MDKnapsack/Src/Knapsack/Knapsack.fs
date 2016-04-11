namespace MDKnapsack

[<Class>]
[<Sealed>]
type Knapsack(capacity : int) = 
    member x.Capacity = capacity
    override x.ToString() = sprintf "knapsack: capacity = %d" capacity
