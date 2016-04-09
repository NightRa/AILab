package analysys

import java.io.{File, FileOutputStream, PrintStream}

import genetic.GeneticAlg
import parametric.Parametric
import parametric.Parametric._
import util.JavaUtil._
import util.Util.avgExecutionTime

class Analysis(name: String,
               geneticParam: Parametric[GeneticAlg[_]],
               rounds: Int,
               intsStepSize: Int,
               doublesStep: Double,
               maxTime: Double) {

  val ints = geneticParam.intNamesDefaults
  val intsMax = geneticParam.intsMax
  val doubles = geneticParam.doubleNamesDefaults

  def main(args: Array[String]) {
    runAnalysis()
  }

  // modify each param at a time away from the optimum.
  def runAnalysis(): Unit = {
    val folder = new File(s"analysis/$name/")
    folder.mkdirs()
    println(s"Outputting analysis to ${folder.getAbsolutePath}")

    for (param <- ints.keys) {
      csvInt(param)
    }

    for (param <- doubles.keys) {
      csvDouble(param)
    }

    println(s"The Analysis is in ${folder.getAbsolutePath}")
  }

  def csvInt(paramName: String): Unit = {
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/${paramName.replace(":", " -")}.csv"))
    for {
      value <- (3 to intsMax(paramName) by intsStepSize).par
    } {
      val alg = geneticParam.applyParams(ints.updated(paramName, value), doubles)
      val time = avgExecutionTime(alg.run(printEvery = 0, maxTime), rounds)
      println(s"Int param '$paramName' = $value, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()
  }

  def csvDouble(paramName: String): Unit = {
    val file = new PrintStream(new FileOutputStream(s"analysis/$name/${paramName.replace(":", " -")}.csv"))
    for {
      value <- (0.0 to 1 by doublesStep).par
    } {
      val alg = geneticParam.applyParams(ints, doubles.updated(paramName, value))
      val time = avgExecutionTime(alg.run(printEvery = 0, maxTime), rounds)
      println(s"Double param '$paramName' = ${formatDouble(value, 3)}, Time = ${formatDouble(time * 1000, 3)} ms.")
      file.println(s"$value, $time")
    }
    file.close()
  }
}

object Analysis {
  def analysis(analysisName: String, geneticParam: Parametric[GeneticAlg[_]]): Parametric[Analysis] =
    for {
      rounds <- intParam("Rounds", default = 10, minValue = 1, maxValue = 100)
      intsStepSize <- intParam("Ints Step Size", default = 10, minValue = 1, maxValue = 100)
      doublesStep <- doubleParam("Doubles Step", 0.01)
      maxTime <- doubleParam("Max Time per run (seconds)", 0.2)
    } yield new Analysis(analysisName, geneticParam, rounds, intsStepSize, doublesStep, maxTime)
}
