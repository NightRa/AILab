using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GuiMDKnapsack.Charting
{
    public sealed class Model
    {
        public double X { get; set; }
        public double Y { get; set; }

        public Model(double x, double y)
        {
            X = x;
            Y = y;
        }
    }
}
