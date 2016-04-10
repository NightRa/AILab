namespace MDKapsack

type IsTaking = 
    | No = 0
    | Yes = 1

[<AutoOpen>]
module IsTakingUtil =
    let isNo = function
        | IsTaking.No -> true
        | IsTaking.Yes -> false
        | _ -> failwith "Incomplete pattern-match"

    let isYes = function
        | IsTaking.No -> false
        | IsTaking.Yes -> true
        | _ -> failwith "Incomplete pattern-match"

    let asString = function
        | IsTaking.No -> "no"
        | IsTaking.Yes -> "yes"
        | _ -> failwith "Incomplete pattern-match"