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
               optimalParams: NamedParams,
               analysisParams: Params) {

  val rounds = analysisParams.ints(0)
  val intsStepSize = analysisParams.ints(1)
  val doublesStep = analysisParams.doubles(0)
  val maxTime = analysisParams.doubles(1)

  def main(args: Array[String]) {
    runAnalysis()
  }

  // modify each param at a time away from the optimum.
  def runAnalysis(): Unit = {
    val folder = new File(s"analysis/$name/")
    folder.mkdirs()
    println(s"Outputting analysis to ${folder.getAbsolutePath}")

    for (index <- 0 until main.intsSize()) {
      csvInt(index)
    }

    for (index <- 0 until main.doublesSize()) {
      csvDouble(index)
    }

    println(s"The Analysis is in ${folder.getAbsolutePath}")
  }

  def csvInt(index: Int): Unit = {
    val paramName = optimalParams.ints(index)._1
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/${paramName.replace(" ", "")}.csv"))
    for {
      value <- (3 to main.intsMax() by intsStepSize).par
    } {
      val params = optimalParams.copy(ints = optimalParams.ints.updated(index, (paramName, value)))
      val time = avgExecutionTime(main.alg(params.toParams, maxTime).run(printEvery = 0), rounds)
      println(s"Int param '$paramName' = $value, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()
  }

  def csvDouble(index: Int): Unit = {
    val paramName = optimalParams.doubles(index)._1
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/${paramName.replace(" ", "")}.csv"))
    for {
      value <- (0.0 to 1 by doublesStep).par
    } {
      val params = optimalParams.copy(doubles = optimalParams.doubles.updated(index, (paramName, value)))
      val time = avgExecutionTime(main.alg(params.toParams, maxTime).run(printEvery = 0), rounds)
      println(s"Double param '$paramName' = ${formatDouble(value, 3)}, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()

  }
}

object Analysis {
  val defaultParams = NamedParams(
    "Rounds" -> 10,
    "Ints Step Size" -> 10
  ) (
    "Doubles Step" -> 0.01,
    "Max Time (seconds)" -> 0.2
  )
}
