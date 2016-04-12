namespace MDKnapsack

open System
open System.IO

type Alg = 
    | DfsSorted = 0
    | DfsNotSorted = 1
    | BestFirst = 2

[<CompilationRepresentation (CompilationRepresentationFlags.ModuleSuffix)>]
module Alg =
    let asString = function
        | Alg.DfsSorted -> "DFS sorted"
        | Alg.DfsNotSorted -> "DFS not sorted"
        | Alg.BestFirst -> "Best first"
        | _ -> failwith "Incomplete pattern"

type BoundKnapsack = 
    | Unbounded = 0
    | Fractional = 1

[<CompilationRepresentation (CompilationRepresentationFlags.ModuleSuffix)>]
module BoundKnapsack =
    let asString = function
        | BoundKnapsack.Unbounded -> "Unbound knapsack"
        | BoundKnapsack.Fractional -> "Bounded knapsack - fractional filled"
        | _ -> failwith "Incomplete pattern"
[<Class>]
[<Sealed>]
type AlgParameters(alg : Alg,
                     boundKnapsack : BoundKnapsack,
                     datFilePaths : string array,
                     algTime : TimeSpan) = 
    do assert (datFilePaths.Length > 0)
    do assert (datFilePaths |> Array.forall (File.Exists))

    member x.Alg = alg
    member x.BoundKnapsack = boundKnapsack
    member x.DatFilePathes = datFilePaths
    member x.AlgTime = algTime
