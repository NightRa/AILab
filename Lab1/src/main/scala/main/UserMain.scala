package main

import java.util.{ArrayList, Random, Scanner}

import analysys.Analysis
import func.{Func, GeneticFuncMain, HoldersTableFunction, LabTestFunction}
import genetic.GeneticMain
import genetic.mating.Crossover
import genetic.types.Population
import knapsack.{GeneticKnapsackMain, Item}
import params.{GeneticParamsMain, NamedParams, Params}
import queens.{GeneticQueenMain, QueenMating, QueenMutation}
import string.{GeneticStringMain, StringHeuristics}
import util.{JavaUtil, Util}

import scala.annotation.tailrec
import scala.util.Try

object UserMain extends App {
  val in = new Scanner(System.in)

  System.out.println("Genetic Algorithms Lab 1 by Ilan Godik & Yuval Alfassi")
  System.out.println()
  runOption()


  def runHillClimbing(): Unit = System.out.println("Not yet supported")

  def runMinimalConflicts(): Unit = System.out.println("Not yet supported")

  def runOption(): Unit = {
    System.out.println(
      """1. Genetic Algorithm
        |2. Hill Climbing - String matching
        |3. Minimal Conflicts - N-Queens""".stripMargin)
    val whatToRun = readIntLoop("Please choose what you want to run: ")

    whatToRun match {
      case 1 =>
        val alg = chooseGeneticAlg()
        menu(alg, alg.defaultParams())
      case 2 => runHillClimbing()
      case 3 => runMinimalConflicts()
      // None or invalid int
      case _ => runOption()
    }
  }

  def chooseGeneticAlg(): GeneticMain[_] = {
    // Choose genetic Alg.
    // Choose variants
    System.out.println("\n" +
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

  def chooseStringAlg(): GeneticMain[_] = {
    // Choose String
    System.out.println("\nChoose your secret: ")
    val secret = in.nextLine()

    // Choose crossover
    def chooseCrossover(): (Array[Char], Array[Char], Random) => Array[Char] = {
      System.out.println(
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

    // Choose heuristic
    @tailrec
    def chooseStringHeuristic(): (Array[Char], Array[Char]) => Double = {
      System.out.println(
        """1. Individual distance heuristic
          |2. Exact matches heuristic
          |3. Exact matches and char-contained matches""".stripMargin)
      val heuristicNum = readIntWithDefault("Choose a string heuristic (default 2): ", 2)
      heuristicNum match {
        case 1 => StringHeuristics.heuristic1
        case 2 => StringHeuristics.heuristic2
        case 3 =>
          val exactsWeight = readIntLoop("Choose a weight for exact matches")
          val containsWeight = readIntLoop("Choose a weight for contained matches")
          StringHeuristics.heuristic3(_, _, containsWeight, exactsWeight)
        case _ => chooseStringHeuristic()
      }
    }

    val heuristic = chooseStringHeuristic()
    new GeneticStringMain(secret, crossover, heuristic)
  }

  def chooseFunctionAlg(): GeneticMain[_] = {
    def chooseFunc(): Func = {
      System.out.println(
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

  def chooseNQueensAlg(): GeneticMain[_] = {
    val boardSize = readIntWithDefault("Choose board size (default 10): ", 10)

    def chooseMating(): (Array[Int], Array[Int], Random) => Array[Int] = {
      System.out.println(
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
      System.out.println(
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

  def chooseKnapsackAlg(): GeneticMain[_] = {
    @tailrec
    def chooseItems(i: Int, items: ArrayList[Item]): Array[Item] = {
      val weight = readDoubleLoop(s"Enter the $i${numSuffix(i)} item's weight (0 to stop): ")
      if (weight == 0) return items.toArray.asInstanceOf[Array[Item]]
      val value = readDoubleLoop(s"Enter the $i${numSuffix(i)} item's value (0 to stop): ")
      if (value == 0) return items.toArray.asInstanceOf[Array[Item]]

      items.add(Item(weight, value))
      chooseItems(i + 1, items)
    }

    val items = chooseItems(1, new ArrayList[Item]())

    val maxWeight = readDoubleLoop("Enter the maximum weight: ")
    System.out.print("Enter the solution if you know it (nothing if not): ")
    val solution = tryReadDouble()

    new GeneticKnapsackMain(items, maxWeight, solution)
  }

  def menu(main: GeneticMain[_], params: NamedParams): Unit = {
    System.out.println("\n" +
      s"""run      - Run ${main.name}
          |params  - Change   Parameters of the Genetic Algorithm
          |opt     - Optimize Parameters of the Genetic Algorithm
          |analyse - Create a statistical report of the Genetic Algorithm
          |bench   - Benchmark the Genetic Algorithm
       """.stripMargin)
    System.out.print("Enter your selection: ")
    val input = in.next()
    input match {
      case "run" =>
        val maxTime = readDoubleWithDefault("Enter the maximum runtime in seconds (default 1.0): ", 1.0) max 0
        runGenetic(main, params.toParams, maxTime)
      case "params" =>
        val newParams = modifyParams(params)
        menu(main, newParams)
      case "opt" =>
        val maxTime = readDoubleWithDefault("Enter the maximum runtime in seconds (default 100.0): ", 100.0) max 0
        optimize(main, maxTime)
      case "analyse" =>
        System.out.println("Enter analysis name: ")
        val name = in.nextLine()
        analysis(name, main, params.toParams, Analysis.defaultParams)
      case "bench" => bench(main, params.toParams)
      case _ => menu(main, params)
    }
  }

  def analysis(name: String, main: GeneticMain[_], params: Params, analysisParams: NamedParams): Unit = {
    System.out.println(
      """run    - Run the analysis
        |params - Change analysis parameters
      """.stripMargin)
    val input = in.next()
    input match {
      case "run" => new Analysis(name, main, params, analysisParams.toParams)
      case "params" =>
        val newAnalysisParams = modifyParams(analysisParams)
        analysis(name, main, params, newAnalysisParams)
      case _ => ()
    }
  }

  def bench(main: GeneticMain[_], params: Params): Unit = {
    val rounds = readIntWithDefault("Enter the number of rounds (1000 default): ", 1000)
    val time = Util.avgExecutionTime(main.alg(params, 1.0).run(print = false), rounds)
    System.out.println(JavaUtil.formatDouble(time * 1000, 4) + " ms")

  }

  def modifyParams(params: NamedParams): NamedParams = {
    System.out.println{
      (params.ints ++ params.doubles).iterator.zipWithIndex.map {
        case ((name, value), index) => s"${index + 1}. $name = $value"
      }.mkString("\n")
    }
    val paramNum = readIntLoop("Which parameter to change? (0 to skip)  ")
    if(paramNum >= 1 && paramNum <= params.ints.length + 1) {
      val index = paramNum - 1
      val paramName = params.ints(index)._1
      val newValue = readIntLoop(s"Set $paramName = ")
      new NamedParams(params.ints.updated(index, (paramName, newValue)), params.doubles)

    } else if(paramNum <= params.ints.length + params.doubles.length + 1) {
      val index = paramNum - params.ints.length - 1
      val paramName = params.doubles(index)._1
      val newValue = readDoubleLoop(s"Set $paramName = ")
      new NamedParams(params.ints, params.doubles.updated(index, (paramName, newValue)))

    } else {
      params
    }
  }

  def runGenetic(main: GeneticMain[_], params: Params, maxTime: Double): Unit = {
    val start = System.currentTimeMillis

    val (population: Population[_], iterations) = main.alg(params, maxTime).run(print = true)

    val end = System.currentTimeMillis
    val time = end - start


    System.out.println("Best 5:")
    System.out.println(population.population.sortBy(_.fitness).take(5).map(_.toString).mkString("\n"))
    System.out.println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + main.seed)
  }

  def optimize(main: GeneticMain[_], maxTime: Double): Unit = {
    val geneticParams = new GeneticParamsMain(main, maxTime)
    runGenetic(geneticParams, geneticParams.defaultParams.toParams, maxTime)
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
    System.out.print(prompt)
    tryReadDouble() match {
      case Some(x) => x
      case None => readDoubleLoop(prompt)
    }
  }

  def tryReadDouble(): Option[Double] = {
    val line = in.next()
    if (line.isEmpty) None
    else Try(line.toDouble).toOption
  }

  def readDoubleWithDefault(prompt: String, default: Double): Double = {
    System.out.print(prompt)
    tryReadDouble().getOrElse(default)
  }

  @tailrec
  def readIntLoop(prompt: String): Int = {
    System.out.print(prompt)
    tryReadInt() match {
      case Some(n) => n
      case None => readIntLoop(prompt)
    }
  }

  def tryReadInt(): Option[Int] = {
    /*val line = in.next()
    if (line.isEmpty) None
    else Try(line.toInt).toOption*/
    Try(in.nextInt()).toOption
  }

  def readIntWithDefault(prompt: String, default: Int): Int = {
    System.out.print(prompt)
    tryReadInt().getOrElse(default)
  }


}
