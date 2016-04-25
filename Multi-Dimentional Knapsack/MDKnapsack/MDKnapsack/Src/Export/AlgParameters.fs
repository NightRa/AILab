namespace MDKnapsack

open System
open System.IO

type Alg = 
    | DfsSorted = 0
    | DfsNotSorted = 1
    | BestFirstNotSorted = 2
    | BestFirstSorted = 3

[<CompilationRepresentation(CompilationRepresentationFlags.ModuleSuffix)>]
module Alg = 
    let asString = 
        function 
        | Alg.DfsSorted -> "DFS" + Environment.NewLine + "sorted"
        | Alg.DfsNotSorted -> "DFS" + Environment.NewLine + "not sorted"
        | Alg.BestFirstNotSorted -> "BFS" + Environment.NewLine + "not sorted"
        | Alg.BestFirstSorted -> "BFS" + Environment.NewLine + "sorted"
        | _ -> failwith "Incomplete pattern"
    let asStringNoNewLine = 
        function 
        | Alg.DfsSorted -> "DFS sorted"
        | Alg.DfsNotSorted -> "DFS not sorted"
        | Alg.BestFirstNotSorted -> "BFS not sorted"
        | Alg.BestFirstSorted -> "BFS sorted"
        | _ -> failwith "Incomplete pattern"

type BoundKnapsack = 
    | Unbounded = 0
    | Fractional = 1

[<CompilationRepresentation(CompilationRepresentationFlags.ModuleSuffix)>]
module BoundKnapsack = 
    let asString b = 
        match b with
        | BoundKnapsack.Unbounded -> "Unbounded"
        | BoundKnapsack.Fractional -> "Bounded"
        | _ -> failwith "Incomplete pattern"

[<Class>]
[<Sealed>]
type AlgParameters(alg : Alg, boundKnapsack : BoundKnapsack, algTime : TimeSpan) = 
    
    static member AllParams(time) : AlgParameters array = 
        [| for alg in [ Alg.DfsNotSorted; Alg.DfsSorted; Alg.BestFirstSorted; Alg.BestFirstNotSorted ] do
               for bound in [ BoundKnapsack.Fractional; BoundKnapsack.Unbounded ] do
                   yield AlgParameters(alg, bound, time) |]
    
    member x.Alg = alg
    member x.BoundKnapsack = boundKnapsack
    member x.AlgTime = algTime
    member x.AsString() = (Alg.asString alg) + Environment.NewLine + (BoundKnapsack.asString boundKnapsack)
    member x.AsStringNoNewLine () = (Alg.asStringNoNewLine alg) + " " + (BoundKnapsack.asString boundKnapsack)
    override x.Equals obj = 
        match obj with
        | (:? AlgParameters as par) -> par.Alg.Equals alg
        | _ -> false
    
    override x.GetHashCode() = (alg, boundKnapsack, algTime).GetHashCode()
