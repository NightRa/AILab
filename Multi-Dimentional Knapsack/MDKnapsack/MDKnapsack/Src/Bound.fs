namespace MDKapsack

module Bound =
    open System
    
    let unboundedKnapsack : (Knapsack -> Item array -> int) =
        fun _ _ -> Int32.MaxValue

    let fractionedFilledKnapsack : (Knapsack -> Item array -> int) = 
        fun knapsack items ->
            items
            |> Seq.ofArray
            |> Seq.sortBy (fun item -> item.Price / item.ConstraintOf knapsack)
            |> Seq.scan (fun (xs, sum) i -> (i::xs, sum + i.Price)) ([],0)
            |> Seq.takeWhile (fun (xs, sum) -> sum < knapsack.Capacity)
            |> Seq.map snd
            |> Seq.last