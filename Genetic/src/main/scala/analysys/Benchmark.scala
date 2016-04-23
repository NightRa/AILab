package analysys

import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticAlg}
import parametric.Parametric
import util.Util

// successes, failures: [(time, best gene, iterations)]
case class BenchmarkResult[A](genetic: Genetic[A], attempts: Int, numSuccesses: Int, successes: Array[(Double, Gene[A], Int)], failures: Array[(Double, Gene[A], Int)]) {
  val bestScore: Double = successes.map(res => genetic.score(res._2)).max
  val successRate: Double = numSuccesses.toDouble / attempts
  val avgSuccessTimeMs: Double = Util.avgBy(successes)(_._1) * 1000
  val avgSuccessIterations: Double = Util.avgBy(successes)(_._3)

  val bestFailureScore: Double = if (failures.length > 0) failures.map(res => genetic.score(res._2)).max else bestScore
  val avgFailureScore: Double = if (failures.length > 0) Util.avgBy(failures)(res => genetic.score(res._2)) else bestScore
  val bestFailureFitness: Double = if (failures.length > 0) failures.map(res => res._2.fitness).min else 0
  val avgFailureFitness: Double = if (failures.length > 0) Util.avgBy(failures)(res => res._2.fitness) else 0
  val avgFailureIterations: Double = if (failures.length > 0) Util.avgBy(failures)(_._3) else 0

  val avgFailurePercent: Double = (avgFailureScore / bestScore) * 100

  def prettyFormat: String = {
    s"""Success Rate: ${(successRate * 100).formatted("%.2f")}%
        |  - $numSuccesses/$attempts
        |
        |Successes:
        |  - Best score: ${bestScore.formatted("%.3f")}
        |  - Avg success time: ${avgSuccessTimeMs.formatted("%.3f")} ms
        |  - Avg success iterations: ${avgSuccessIterations.formatted("%.1f")}
        """.stripMargin + "\n" + {
      if (failures.length > 0) {
        s"""Failures:
            |  - Highest score achieved: ${bestFailureScore.formatted("%.3f")}
            |  - Avg score achieved: ${avgFailureScore.formatted("%.3f")}
            |  - Avg score percent of best: ${avgFailurePercent.formatted("%.3f")}
            |  - Best fitness achieved: ${bestFailureFitness.formatted("%.3f")}
            |  - Avg fitness achieved: ${avgFailureFitness.formatted("%.4f")}
            |  - Avg iterations: ${avgFailureIterations.formatted("%.1f")}
        """.stripMargin
      }
      else
        ""
    }
  }

  def csvFormat(problemName: String): String = {
    List(problemName, successRate, avgSuccessTimeMs, bestScore, avgSuccessIterations, bestFailureScore, avgFailureScore, avgFailurePercent, bestFailureFitness, avgFailureFitness, avgSuccessIterations).mkString(",")
  }
}

object Benchmark {

  def csvHeading: String =
    "Problem name, Success Rate, Avg success time (ms), Success Score, Avg Success Iterations, Best failure score, Avg failure score, Avg Failure Percent of best, Best failure fitness, Avg failure fitness, Avg failure iterations"

  def benchmark[A](geneticAlg: Parametric[GeneticAlg[A]], attempts: Int, maxTime: Double): BenchmarkResult[A] = {
    val results = (0 until attempts).par.map {
      i =>
        val alg = geneticAlg.applyDefaults()
        val (time: Double, (population: Population[_], iterations: Int)) = Util.timeExecution(alg.run(0, maxTime))
        val isSuccess = (maxTime - time) > GeneticAlg.epsilon
        (time,
          population.population(0),
          iterations,
          isSuccess)

    }
    val successTimes = results.collect {
      case (time, gene, iterations, true) => (time, gene, iterations)
    }.toArray

    val failureScores = results.collect {
      case (time, gene, iterations, false) => (time, gene, iterations)
    }.toArray

    BenchmarkResult[A](geneticAlg.applyDefaults().genetic, attempts, successTimes.length, successTimes, failureScores)
  }
}
