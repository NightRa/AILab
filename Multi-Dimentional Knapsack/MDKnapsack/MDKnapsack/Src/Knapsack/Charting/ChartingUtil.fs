namespace MDKnapsack


module ChartingUtil =
    open FSharp.Charting.ChartTypes
    open FSharp.Charting
    open System.Threading
    open System.Windows.Forms

    (*let asCombinedChart (data) (mapper) : GenericChart =
        let numOfInnerItems = float ((snd (data |> Array.head)) |> Array.length)
        let numOfItems = data.Length
        let numOfItemsFloat = float numOfItems
        let fraction = 1.0 / numOfItemsFloat
        let mutable shift2 = 2.0 * fraction
        let mutable charts = []
        for key, values in data do
           values
           |> Array.zip [|0.0 .. fraction .. 1.0|]
           |> Array.map (fun (shift1, value) -> (shift1 + shift2, mapper value))
           |> Array.toSeq
           |> fun myData -> Chart.Column myData
           |> fun chart -> charts <- chart :: charts
        Chart.Combine charts*)
    let asSingleChartColumn chartName (names : string[], results : float[]) : GenericChart =
        let chart = Chart.Column (results, Name = chartName, Labels = names)
        let chart = chart.WithDataPointLabels (LabelPosition = LabelPosition.Bottom)
        let chart = chart.WithXAxis (Enabled = false)
        let chart = chart.WithYAxis (Enabled = true, Title = "Price Percent", Min = 0.0, Max = 1.0, TitleFontSize = 16.0)
        chart

    let asSingleChartLine chartName (names : string[], results : float[]) : GenericChart =
        let chart = Chart.Line (results, Name = chartName, Labels = names)
        let chart = chart.WithDataPointLabels (LabelPosition = LabelPosition.Bottom)
        let chart = chart.WithXAxis (Enabled = false)
        let chart = chart.WithYAxis (Enabled = true, Title = "Price Percent", Min = 0.0, Max = 1.0, TitleFontSize = 16.0)
        chart

    let showChartBlocking (chart : GenericChart) = 
        let form = chart.ShowChart ()
        form.Refresh ()
        for i = 0 to 100000 do
            Application.DoEvents ()
            Thread.Sleep 2
        ()

        
        

