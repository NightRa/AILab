package analysys

import java.io.{File, FileOutputStream, PrintStream}

import func.GeneticFuncMain
import genetic.GeneticMain
import params.Params
import util.JavaUtil._
import util.Util.avgExecutionTime

class Analysis(name: String, main: GeneticMain[_], optimalParams: Params) {
  val intsStepSize = 100
  val doublesStep = 0.005
  val maxTime = 1.0
  val rounds = 100

  // modify each param at a time away from the optimum.
  def main(args: Array[String]) {
    new File(s"analysis/$name/").mkdirs()

    for (index <- 0 until main.intsSize()) {
      csvInt(index)
    }

    for (index <- 0 until main.doublesSize()) {
      csvDouble(index)
    }
  }

  def csvInt(index: Int): Unit = {
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/int-$index.csv"))
    for {
      value <- (100 to main.intsMax() by intsStepSize).par
    } {
      val params = optimalParams.copy(ints = optimalParams.ints.updated(index, value))
      val time = avgExecutionTime(main.alg(params, maxTime).run(print = false), rounds)
      println(s"Int param $index = $value, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()
  }

  def csvDouble(index: Int): Unit = {
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/double-$index.csv"))
    for {
      value <- (0.0 to 1.0 by doublesStep).par
    } {
      val params = optimalParams.copy(doubles = optimalParams.doubles.updated(index, value))
      val time = avgExecutionTime(main.alg(params, maxTime).run(print = false), rounds)
      println(s"Double param $index = ${formatDouble(value, 3)}, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()

  }
}

object AnalysisFunc extends Analysis("Func2", GeneticFuncMain, GeneticFuncMain.defaultParams)
