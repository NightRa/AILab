namespace MDKnapsack

module Bound = 
    open System
    
    let unboundedKnapsack : Item[] -> Knapsack[] -> int = fun _ _ -> Int32.MaxValue
    
    // TODO: Check a 10000 times. test, refine + define
    let fractionedFilledKnapsack (items : Item array) (knapsack : Knapsack) : int = 
        let length = items.Length
        let items = items |> Array.sortBy (fun item -> (float32 <| item.ConstraintOf knapsack) / (float32 item.Price))
        let mutable index = 0
        let mutable filled = 0
        let mutable totalPrice = 0
        while index < length && filled < knapsack.Capacity do
            let constr = items.[index].ConstraintOf knapsack
            if filled + constr > knapsack.Capacity then 
                let price = float32 <| items.[index].Price
                let rest = float32 <| knapsack.Capacity - filled
                let percent = rest / (float32 constr)
                totalPrice <- totalPrice + int32 (percent * price)
                index <- length
            else 
                filled <- filled + constr
                totalPrice <- totalPrice + items.[index].Price
                index <- index + 1
        totalPrice
