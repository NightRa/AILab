namespace MDKnapsack.Main

module Program = 
    open System
    open System.IO
    open MDKnapsack
    open MDKnapsack.Util
    
    let mutable filesDirPath = @"C:\Users\Yuval\Desktop\samples"
    let mutable csvOutputPath = @"C:\Users\Yuval\Desktop\AI.csv"
    let mutable time = TimeSpan.FromSeconds 1.0
    let allParams () = AlgParameters.AllParams time
    let paramsStr () = allParams() |> Array.map (fun p -> p.AsStringNoNewLine ()) |> Array.mkString (",")
    let header1 () = "Name" + "," + paramsStr() + "," + paramsStr()
    let percentStr () = allParams() |> Array.map (fun _ -> "percent") |> Array.mkString ","
    let timeStr () = allParams() |> Array.map (fun _ -> "time") |> Array.mkString ","
    let header2 () = "," + percentStr() + "," + timeStr() + newLine + header1()
    let asString = function
        | None -> "false"
        | Some (time : TimeSpan) -> time.TotalSeconds.ToString ("0.000")

    let resOnParams par = 
        filesDirPath
        |> Directory.EnumerateFiles
        |> Seq.filter (fun f -> f.ToUpper().EndsWith ".DAT")
        |> Seq.toArray
        |> Array.Parallel.map (fun f -> (Path.GetFileNameWithoutExtension f, RunAlgorithm.runAlgorithmSingle (par, f, new Action(fun () -> ()))))
        |> Seq.ofArray
        |> Seq.map (fun (f, (p, sol, t, _)) -> (f, t, 100.0 * float sol.Price / float p.Optimal ))
        |> Seq.map (fun (f, t, x) -> (f, asString t, x.ToString ("##.00")))
        |> Seq.toArray
    
    [<EntryPoint>]
    let main argv =
        printfn "Enter samples directory path"
        //filesDirPath <- Console.ReadLine ()
        printfn "Enter output csv directory path"
       // csvOutputPath <- Path.Combine (Console.ReadLine (), "AI.csv")
        printfn "Enter time for each alg"
        //time <- TimeSpan.FromSeconds (Double.Parse (Console.ReadLine ()))

        let get1  (a, _, _) = a
        let get23 (_, b, c) = (b, c)
        let uncurry f = fun (a,b) -> f a b
        let inv (a,b) = (b,a)
        allParams()
        |> Array.map resOnParams
        |> Array.inverse
        |> Array.map (fun a -> (get1 a.[0], a |> Array.map get23 |> Array.unzip |> inv |> uncurry Array.append))
        |> Array.map (fun (name, rest) -> name + "," + (Array.mkString "," rest))
        |> Array.mkString (newLine)
        |> fun lines -> header2() + newLine + lines
        |> fun lines -> File.WriteAllText(csvOutputPath, lines)
        
        0
