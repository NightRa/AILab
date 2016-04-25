namespace MDKnapsack

module Bound = 
    open System

    let unboundedKnapsack (sol : Solution) =
        let mutable restPotentialPrice = 0
        for i = sol.OpenBits to sol.Prob.Items.Length - 1 do
            restPotentialPrice <- restPotentialPrice + sol.Prob.Items.[i].Price
        sol.Price + restPotentialPrice
    

    let fractionedFilledKnapsack (sol : Solution) (knapsack : Knapsack) : int =
        let openedBits = sol.OpenBits
        let items = sol.Prob.Items 
                    |> Array.skip openedBits
                    |> Array.sortBy (fun item -> (float32 <| item.ConstraintOf knapsack) / (float32 item.Price))
        let length = items.Length
        let mutable index = 0
        let mutable filled = 0
        let mutable totalPrice = 0
        for i = 0 to openedBits - 1 do
            if sol.ItemsTaken.[i] then
                filled <- filled + sol.Prob.Items.[i].ConstraintOf knapsack
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
            ()
        sol.Price + totalPrice

    
    let upperBoundFractional (sol : Solution) : int =
        sol.Prob.Knapsacks
        |> Seq.ofArray
        |> Seq.map (fun k -> fractionedFilledKnapsack sol k)
        |> Seq.min

    let partialDensity = upperBoundFractional
        