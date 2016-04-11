using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GuiMDKnapsack
{
    public enum Alg : byte
    {
        Dfs, Bfs
    }
    public enum KnapsackBound : byte
    {
        Unbound, Fractional
    }
    public enum SortItems : byte
    {
        Sort, DontSort
    }
    public class AlgParameters
    {
        public readonly string[] datFilePathes;
        public readonly Alg alg;
        public readonly KnapsackBound bound;
        public readonly SortItems sortItems;
        public readonly TimeSpan runningTime;

        public AlgParameters(string[] datFilePathes, Alg alg, SortItems sortItems, KnapsackBound bound, TimeSpan runningTime)
        {
            Debug.Assert(datFilePathes.Length >= 1);
            Debug.Assert(datFilePathes.All(File.Exists));
            this.datFilePathes = datFilePathes.ToArray();
            this.alg = alg;
            this.sortItems = sortItems;
            this.bound = bound;
            this.runningTime = runningTime;
        }
    }
}
