
namespace MDKapsack.Util

module Dictionary =

    let from (keys : 'a array) (values : 'b array) =
        assert (keys.Length = values.Length)
        let dict = new System.Collections.Generic.Dictionary<_,_> ()
        for i = 0 to keys.Length - 1 do
            dict.Add (keys.[i], values.[i])
        dict

namespace MDKapsack.Util
module Array =
    let inverse (a : 'a[][]) : 'a[][] =
        let rows = a.Length
        let cols = a.[0].Length
        Array.init cols (fun i -> Array.init rows (fun j -> a.[j].[i]))


namespace MDKapsack.Util
module List =
    let rec mkString seperator = function
        | [] -> ""
        | [x] -> x.ToString ()
        | x::xs -> x.ToString() + seperator + (mkString seperator xs)

