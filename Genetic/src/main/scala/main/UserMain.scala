/*
package main

import java.util.{ArrayList, Random, Scanner}

import analysys.Analysis
import func.{Func, GeneticFuncMain, HoldersTableFunction, LabTestFunction}
import genetic.GeneticMetadata
import genetic.generation.Crossover
import genetic.types.Population
import knapsack.{GeneticKnapsackMain, Item}
import params.{GeneticParamsMain, NamedParams, Params}
import queens.{GeneticQueenMain, QueenMating, QueenMutation}
import string.{GeneticStringMain, HillClimbing, StringHeuristics}
import util.{JavaUtil, Util}

import scala.annotation.tailrec
import scala.util.Try

object UserMain extends App {
  val in = new Scanner(System.in)

  println()
  println("Genetic Algorithms Lab 1 by Ilan Godik & Yuval Alfassi")

  mainMenu()

  println("Press any key to exit")
  in.nextLine()

  def mainMenu(): Unit = {
    println()
    println(
      """1. Genetic Algorithm
        |2. Hill Climbing - String matching""".stripMargin)
    // |3. Minimal Conflicts - N-Queens
    val whatToRun = readIntLoop("Please choose what you want to run: ")

    whatToRun match {
      case 1 =>
        val alg = chooseGeneticAlg()
        menu(alg, alg.defaultParams())
      case 2 => runHillClimbing()
      // case 3 => runMinimalConflicts()
      // None or invalid int
      case _ => mainMenu()
    }
  }

  // def runMinimalConflicts(): Unit = println("Not yet supported")

  def runHillClimbing(): Unit = {
    println("Choose your secret: ")
    val secret = in.nextLine()
    println()
    val heuristic = chooseStringHeuristic()
    println()
    val printInt = readIntWithDefault("Print intermediate states? (0 / 1): ", 1)
    val print =
      if (printInt == 1) true
      else if (printInt == 0) false
      else true
    HillClimbing.run(secret, heuristic, print)
    mainMenu()
  }

  def chooseGeneticAlg(): GeneticMetadata[_] = {
    // Choose genetic Alg.
    // Choose variants
    println("\n" +
      """1. String searching
        |2. Function optimization
        |3. N-Queens
        |4. Knapsack""".stripMargin)
    val algNum = readIntLoop("What problem do you want to solve? ")
    algNum match {
      case 1 => chooseStringAlg()
      case 2 => chooseFunctionAlg()
      case 3 => chooseNQueensAlg()
      case 4 => chooseKnapsackAlg()
      case _ => chooseGeneticAlg()
    }
  }

  @tailrec
  def chooseStringHeuristic(): (Array[Char], Array[Char]) => Double = {
    println(
      """1. Individual distance heuristic
        |2. Exact matches heuristic
        |3. Exact matches and char-contained matches""".stripMargin)
    val heuristicNum = readIntWithDefault("Choose a string heuristic (default 2): ", 2)
    heuristicNum match {
      case 1 => StringHeuristics.heuristic1
      case 2 => StringHeuristics.heuristic2
      case 3 =>
        val exactsWeight = readIntLoop("Choose a relative weight for exact matches (int): ")
        val containsWeight = readIntLoop("Choose a relative weight for contained matches (int): ")
        StringHeuristics.heuristic3(_, _, containsWeight, exactsWeight)
      case _ => chooseStringHeuristic()
    }
  }

  def chooseStringAlg(): GeneticMetadata[_] = {
    // Choose String
    println("\nChoose your secret: ")
    val secret = in.nextLine()

    println()

    // Choose crossover
    def chooseCrossover(): (Array[Char], Array[Char], Random) => Array[Char] = {
      println(
        """1. One Point Crossover
          |2. Two Point Crossover
          |3. Uniform   Crossover""".stripMargin)
      val crossoverNum = readIntWithDefault("Choose a crossover strategy (default 1): ", 1)
      crossoverNum match {
        case 1 => Crossover.onePointCrossoverString
        case 2 => Crossover.twoPointCrossoverString
        case 3 => Crossover.uniformCrossoverString
      }
    }

    val crossover = chooseCrossover()

    println()

    val heuristic = chooseStringHeuristic()
    new GeneticStringMain(secret, crossover, heuristic)
  }

  def chooseFunctionAlg(): GeneticMetadata[_] = {
    def chooseFunc(): Func = {
      println(
        """1. Lab test Function
          |2. Holder's table Function
        """.stripMargin)
      val funcNum = readIntWithDefault("Choose a function (default 2): ", 2)
      funcNum match {
        case 1 => LabTestFunction
        case 2 => HoldersTableFunction
        case _ => chooseFunc()
      }
    }

    val func = chooseFunc()
    new GeneticFuncMain(func)
  }

  def chooseNQueensAlg(): GeneticMetadata[_] = {
    val boardSize = readIntWithDefault("Choose board size (default 10): ", 10)

    def chooseMating(): (Array[Int], Array[Int], Random) => Array[Int] = {
      println(
        """1. PMX - Partially Matched Crossover
          |2. OX  - Ordered Crossover
          |3. CX  - Cycle   Crossover
        """.stripMargin)
      val matingNum = readIntLoop("Choose a mating algorithm: ")
      matingNum match {
        case 1 => QueenMating.pmx
        case 2 => QueenMating.ox
        case 3 => QueenMating.cx
        case _ => chooseMating()
      }
    }

    val mating = chooseMating()

    def chooseMutation(): (Array[Int], Random) => Unit = {
      println(
        """1. Displacement
          |2. Exchange
          |3. Insertion
          |4. Simple Inversion
          |5. Complex Inversion
          |6. Scramble
        """.stripMargin)
      val mutationNum = readIntLoop("Choose a mutation algorithm: ")
      mutationNum match {
        case 1 => QueenMutation.displacement
        case 2 => QueenMutation.exchange
        case 3 => QueenMutation.insertion
        case 4 => QueenMutation.simpleInversion
        case 5 => QueenMutation.complexInversion
        case 6 => QueenMutation.scramble
        case _ => chooseMutation()
      }
    }

    val mutation = chooseMutation()

    new GeneticQueenMain(boardSize, mating, mutation)
  }

  def chooseKnapsackAlg(): GeneticMetadata[_] = {
    println()
    @tailrec
    def chooseItems(i: Int, items: ArrayList[Item]): Array[Item] = {
      val weight = readDoubleLoop(s"Enter the $i${numSuffix(i)} item's weight (0 to stop): ")
      if (weight == 0) return items.toArray(Array.empty[Item])
      val value = readDoubleLoop(s"Enter the $i${numSuffix(i)} item's value (0 to stop): ")
      if (value == 0) return items.toArray(Array.empty[Item])

      items.add(Item(weight, value))
      chooseItems(i + 1, items)
    }

    val items = chooseItems(1, new ArrayList[Item]())

    val maxWeight = readDoubleLoop("Enter the maximum weight: ")
    print("Enter the solution if you know it (nothing if not): ")
    val solution = tryReadDouble()

    new GeneticKnapsackMain(items, maxWeight, solution)
  }

  def menu(main: GeneticMetadata[_], params: NamedParams): Unit = {
    println("\n" +
      s"""|1. run     - Run ${main.name}
          |2. params  - Change   Parameters of the Genetic Algorithm
          |3. opt     - Optimize Parameters of the Genetic Algorithm
          |4. analyse - Create a statistical report of the Genetic Algorithm
          |5. bench   - Benchmark the Genetic Algorithm
          |6. main    - Return to the main menu
       """.stripMargin)
    print("Enter your selection: ")
    val input = in.nextLine()
    input match {
      case "run" | "1" =>
        val maxTime = readDoubleWithDefault("Enter the maximum runtime in seconds (default 1.0): ", 1.0) max 0
        val defaultPrintEvery = main.defaultPrintEvery()
        val printEvery = readIntWithDefault(s"Print best every how many iterations? (default $defaultPrintEvery, 0 for never) ", defaultPrintEvery) max 0
        runGenetic(main, params.toParams, maxTime, printEvery)
        menu(main, params)
      case "params" | "2" =>
        val newParams = modifyParams(params)
        menu(main, newParams)
      case "opt" | "3" =>
        val maxTime = readDoubleWithDefault("Enter the maximum runtime in seconds (default 100.0): ", 100.0) max 0
        optimize(main, maxTime)
        menu(main, params)
      case "analyse" | "4" =>
        println("Enter analysis name: ")
        val name = in.nextLine()
        analysis(name, main, params, Analysis.defaultParams)
      case "bench" | "5" =>
        bench(main, params.toParams)
        menu(main, params)
      case "main" | "6" =>
        mainMenu()
      case _ => menu(main, params)
    }
  }

  def analysis(name: String, main: GeneticMetadata[_], params: NamedParams, analysisParams: NamedParams): Unit = {
    println()
    println(
      """run    - Run the analysis
        |params - Change analysis parameters
      """.stripMargin)
    print("Enter your selection: ")
    val input = in.nextLine()
    input match {
      case "run" =>
        new Analysis(name, main, params, analysisParams.toParams).runAnalysis()
        menu(main, params)
      case "params" =>
        val newAnalysisParams = modifyParams(analysisParams)
        analysis(name, main, params, newAnalysisParams)
      case _ =>
        analysis(name, main, params, analysisParams)
    }
  }

  def bench(main: GeneticMetadata[_], params: Params): Unit = {
    val rounds = readIntWithDefault("Enter the number of rounds (1000 default): ", 1000)
    val time = Util.avgExecutionTime(main.genetic(params).run(printEvery = 0, 0.3), rounds)
    println(JavaUtil.formatDouble(time * 1000, 4) + " ms")
  }

  def modifyParams(params: NamedParams): NamedParams = {
    println {
      (params.ints ++ params.doubles).iterator.zipWithIndex.map {
        case ((name, value), index) => s"${index + 1}. $name = $value"
      }.mkString("\n")
    }
    val paramNum = readIntLoop("Which parameter to change? (0 to skip)  ")
    if (paramNum == 0)
      params
    else if (paramNum >= 1 && paramNum < params.ints.length + 1) {
      val index = paramNum - 1
      val paramName = params.ints(index)._1
      val newValue = readIntLoop(s"Set $paramName = ")
      new NamedParams(params.ints.updated(index, (paramName, newValue)), params.doubles)

    } else if (paramNum < params.ints.length + params.doubles.length + 1) {
      val index = paramNum - params.ints.length - 1
      val paramName = params.doubles(index)._1
      val newValue = readDoubleLoop(s"Set $paramName = ")
      new NamedParams(params.ints, params.doubles.updated(index, (paramName, newValue)))

    } else {
      params
    }
  }

  def runGenetic[A](main: GeneticMetadata[A], params: Params, maxTime: Double, printEvery: Int): Unit = {
    val alg = main.genetic(params)

    val start = System.currentTimeMillis

    val (population: Population[_], iterations) = alg.run(printEvery, maxTime)

    val end = System.currentTimeMillis
    val time = end - start

    val popSize = population.population.length
    println(s"Best ${5 min popSize}:")
    println(population.population.sortBy(_.fitness).take(5).map(gene => alg.genetic.show(gene.gene) + ", fitness = " + gene.fitness).mkString("\n"))
    println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + main.seed)
  }

  def optimize(main: GeneticMetadata[_], maxTime: Double): Unit = {
    val geneticParams = new GeneticParamsMain(main, maxTime)
    runGenetic(geneticParams, geneticParams.defaultParams.toParams, maxTime, printEvery = 1)
  }

  // ---------------------------------------------------------------------------------------------------------
  // Input Helpers

  def numSuffix(n: Int): String = {
    n.toString.last match {
      case '1' => "st"
      case '2' => "nd"
      case '3' => "rd"
      case _ => "th"
    }
  }

  def readDoubleLoop(prompt: String): Double = {
    print(prompt)
    tryReadDouble() match {
      case Some(x) => x
      case None => readDoubleLoop(prompt)
    }
  }

  def tryReadDouble(): Option[Double] = {
    val line = in.nextLine()
    if (line.isEmpty) None
    else Try(line.toDouble).toOption
  }

  def readDoubleWithDefault(prompt: String, default: Double): Double = {
    print(prompt)
    tryReadDouble().getOrElse(default)
  }

  @tailrec
  def readIntLoop(prompt: String): Int = {
    print(prompt)
    tryReadInt() match {
      case Some(n) => n
      case None => readIntLoop(prompt)
    }
  }

  def tryReadInt(): Option[Int] = {
    val line = in.nextLine()
    if (line.isEmpty) None
    else Try(line.toInt).toOption
  }

  def readIntWithDefault(prompt: String, default: Int): Int = {
    print(prompt)
    tryReadInt().getOrElse(default)
  }


}
*/
