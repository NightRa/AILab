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
using FSharp.Charting;
using MDKnapsack;
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
            this.Cursor = Cursors.Wait;
            string[] filenames = maybeFileNames.Value;
            TimeSpan runningTime = maybeRunTime.Value;
            if (AnalizeCheckBox.IsChecked.GetValueOrDefault(false))
                Analization(filenames[0], runningTime);
            else
            {
                Alg alg = GetAlg();
                BoundKnapsack bound = GetBound();
                var parameters = new AlgParameters(alg, bound, runningTime);
                if (filenames.Length == 1)
                    RunWithParameters(parameters, filenames[0]);
                else
                    RunWith(parameters, filenames);
            }
            this.Cursor = Cursors.Arrow;
        }

        private void RunWith(AlgParameters parameters, string[] filenames)
        {
            CatchErrors(() =>
            {
                var chart = RunAlgorithm.runAlgorithmMultipleDats(parameters, filenames, Respond);
                Chart.Show(chart);
            });
        }

        private void Analization(string filename, TimeSpan runningTime)
        {
            CatchErrors(() =>
            {
                var parameters = AlgParameters.AllParams(runningTime);
                var chart = RunAlgorithm.runAlgorithmOnParams(parameters, filename, Respond);
                Chart.Show(chart);
            });
        }

        private void RunWithParameters(AlgParameters parameters, string filename)
        {
            CatchErrors(() =>
            {
                var result = RunAlgorithm.runAlgorithmSingle(parameters, filename, Respond);
                string[] data = RunAlgorithm.asString(Tuple.Create(result));
                ListView view = new ListView();
                view.FontSize = 14;
                foreach (var str in data)
                    view.Items.Add(str);
                var window = new Window();
                window.Content = view;
                window.ShowInTaskbar = false;
                window.Show();
            });
        }

        private Alg GetAlg()
        {
            if (DfsAlg.IsChecked.GetValueOrDefault(false))
                return Alg.DfsNotSorted;
            if (BfsAlg.IsChecked.GetValueOrDefault(false))
                return Alg.BestFirst;
            if (DfsSortedAlg.IsChecked.GetValueOrDefault(false))
                return Alg.DfsSorted;
            throw new Exception("Programming Exception");
        }

        private BoundKnapsack GetBound()
        {
            if (UnboundKnapsack.IsChecked.GetValueOrDefault(false))
                return BoundKnapsack.Unbounded;
            if (FractionalKnapsack.IsChecked.GetValueOrDefault(false))
                return BoundKnapsack.Fractional;
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

        private void AnalizeCheckBox_OnChecked(object sender, RoutedEventArgs e)
        {
            var dec = TextDecorations.Underline[0];
            if (AnalizeText.TextDecorations.Contains(dec))
                AnalizeText.TextDecorations.Remove(dec);
            else
                AnalizeText.TextDecorations.Add(dec);
            foreach (var radioButton in AllRadioButtons(MainGrid))
                radioButton.IsEnabled = radioButton.IsEnabled ^ true;
        }

        private IEnumerable<RadioButton> AllRadioButtons(Panel panel)
        {
            foreach (var radioButton in panel.Children.OfType<RadioButton>())
                yield return radioButton;
            foreach (var innerPannel in panel.Children.OfType<Panel>())
                foreach (var radioButton in AllRadioButtons(innerPannel))
                    yield return radioButton;

        }

        private void CatchErrors(Action action)
        {
            try
            {
                action();
            }
            catch (Exception exeption)
            {
                MessageBox.Show("An error occurred: " + Environment.NewLine + exeption);
            }
        }

        private void Respond()
        {
            this.Dispatcher.InvokeAsync(() => { });
        }
    }


}
