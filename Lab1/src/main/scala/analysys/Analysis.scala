package analysys

import java.io.{File, FileOutputStream, PrintStream}

import func.GeneticFuncMain
import genetic.GeneticMain
import knapsack.GeneticKnapsackMain
import params.{NamedParams, Params}
import queens.GeneticQueenMain
import string.GeneticStringMain
import util.JavaUtil._
import util.Util.avgExecutionTime

class Analysis(name: String,
               main: GeneticMain[_],
               optimalParams: Params,
               analysisParams: Params) {

  val rounds = analysisParams.ints(0)
  val intsStepSize = analysisParams.ints(1)
  val doublesStep = analysisParams.doubles(0)
  val maxTime = analysisParams.doubles(1)

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
      value <- (3 to main.intsMax() by intsStepSize).par
    } {
      val params = optimalParams.copy(ints = optimalParams.ints.updated(index, value))
      val time = avgExecutionTime(main.alg(params, maxTime).run(printEvery = 0), rounds)
      println(s"Int param $index = $value, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()
  }

  def csvDouble(index: Int): Unit = {
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/double-$index.csv"))
    for {
      value <- (0.0 to 1 by doublesStep).par
    } {
      val params = optimalParams.copy(doubles = optimalParams.doubles.updated(index, value))
      val time = avgExecutionTime(main.alg(params, maxTime).run(printEvery = 0), rounds)
      println(s"Double param $index = ${formatDouble(value, 3)}, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()

  }
}

object Analysis {
  val defaultParams = NamedParams(
    "Rounds" -> 100,
    "Ints Step Size" -> 10
  ) (
    "Doubles Step" -> 0.005,
    "Max Time (seconds)" -> 0.2
  )
}
