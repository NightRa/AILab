namespace MDKnapsack.Main

module Program = 
    open System
    open System.IO
    open MDKnapsack
    open MDKnapsack.Util
    
    let filesDirPath = @"samples"
    let csvOutputPath = @"AI.csv"
    let time = TimeSpan.FromSeconds 20.0
    let paramsDfs = AlgParameters(Alg.DfsNotSorted, BoundKnapsack.Unbounded, time)
    let paramBfs = AlgParameters(Alg.BestFirst, BoundKnapsack.Unbounded, time)
    
    let asString = function
        | None -> "false"
        | Some (time : TimeSpan) -> time.TotalSeconds.ToString ()

    let resOnParams par = 
        filesDirPath
        |> Directory.EnumerateFiles
        |> Seq.filter (fun f -> f.ToUpper().EndsWith ".DAT")
        |> Seq.toArray
        |> Array.Parallel.map (fun f -> (Path.GetFileNameWithoutExtension f, RunAlgorithm.runAlgorithmSingle (par, f, new Action(fun () -> ()))))
        |> Seq.ofArray
        |> Seq.map (fun (f, (p, sol, t, _)) -> (f, t, 100.0 * float sol.Price / float p.Optimal ))
        |> Seq.map (fun (f, t, x) -> (f, asString t, x))
        |> Seq.toList
    
    [<EntryPoint>]
    let main argv = 
        let dfsResults = resOnParams paramsDfs
        let bfsResults = resOnParams paramBfs
        
        let lines = 
            List.zip dfsResults bfsResults
            |> List.map (fun ((f, a, x), (_, b, y)) -> (f, a.ToString(), b.ToString(), x.ToString(), y.ToString()))
            |> fun l -> ("Names", "DFS", "BFS", "DFS percent", "BFS percent") :: l
            |> List.map (fun (a, b, c, d, e) -> a + "," + b + "," + c + "," + d + "," + e)
            |> List.toSeq
        File.WriteAllLines(csvOutputPath, lines)
        0
