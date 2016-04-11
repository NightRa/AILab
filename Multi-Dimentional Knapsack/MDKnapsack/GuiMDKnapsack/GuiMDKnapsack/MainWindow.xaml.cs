using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using IOPath = System.IO.Path;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.FSharp.Core;
using Microsoft.Win32;

namespace GuiMDKnapsack
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private FSharpOption<string[]> maybeFileNames;
        public MainWindow()
        {
            InitializeComponent();
        }

        private void SetFileNames(string[] filenames)
        {
            if (filenames.Length > 0 && filenames.All(File.Exists))
            {
                maybeFileNames = FSharpOption<string[]>.Some(filenames);
                this.DatFilesListView.Items.Clear();
                foreach (var filename in filenames.Select(IOPath.GetFileName))
                    this.DatFilesListView.Items.Add(filename);
            }
        }

        private void ChooseDatFilesButton_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dlg = new OpenFileDialog { DefaultExt = ".DAT", Multiselect = true };

            var result = dlg.ShowDialog();

            if (result.HasValue && result == true)
            {
                var filenames = dlg.FileNames;
                if (filenames.Length > 0)
                    SetFileNames(filenames);
            }
        }

        private void RunButton_Click(object sender, RoutedEventArgs e)
        {
            if (FSharpOption<string[]>.get_IsNone(maybeFileNames))
            {
                MessageBox.Show("You didnt select input DAT files for the knapsack algorithm");
                return;
            }
            var maybeRunTime = GetAlgRunTime();
            if (FSharpOption<TimeSpan>.get_IsNone(maybeRunTime))
            {
                MessageBox.Show("Not a valid value of alg run time");
                return;
            }
            TimeSpan runningTime = maybeRunTime.Value;
            string[] filenames = maybeFileNames.Value;
            Alg alg = GetAlg();
            KnapsackBound bound = GetBound();
            SortItems sortItems = GetSortItems();
            AlgParameters parameters = new AlgParameters(filenames, alg, sortItems, bound, runningTime);
            RunWithParameters(parameters);
        }

        private void RunWithParameters(AlgParameters parameters)
        {
            //throw new NotImplementedException();
        }

        private SortItems GetSortItems()
        {
            if (SortedItems.IsChecked.GetValueOrDefault(false))
                return SortItems.Sort;
            if (UnSortedItems.IsChecked.GetValueOrDefault(false))
                return SortItems.DontSort;
            throw new Exception("Programming Exception");
        }

        private Alg GetAlg()
        {
            if (DfsAlg.IsChecked.GetValueOrDefault(false))
                return Alg.Dfs;
            if (BfsAlg.IsChecked.GetValueOrDefault(false))
                return Alg.Bfs;
            throw new Exception("Programming Exception");
        }

        private KnapsackBound GetBound()
        {
            if (UnboundKnapsack.IsChecked.GetValueOrDefault(false))
                return KnapsackBound.Unbound;
            if (FractionalKnapsack.IsChecked.GetValueOrDefault(false))
                return KnapsackBound.Fractional;
            throw new Exception("Programming Exception");
        }

        private FSharpOption<TimeSpan> GetAlgRunTime()
        {
            double res;
            if (double.TryParse(AlgRunTime.Text, out res))
            {
                if (res < 0)
                    return FSharpOption<TimeSpan>.None;
                return FSharpOption<TimeSpan>.Some(TimeSpan.FromSeconds(res));
            }
            return FSharpOption<TimeSpan>.None;
        } 
    }
}
