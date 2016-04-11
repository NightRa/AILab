namespace MDKnapsack

open System
open System.IO

type Alg = 
    | Dfs = 0
    | Bfs = 1

type BoundKnapsack = 
    | Unbounded = 0
    | Fractional = 1

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
