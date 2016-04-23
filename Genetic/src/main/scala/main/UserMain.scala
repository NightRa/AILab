package main

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util.{ArrayList, Random, Scanner}

import analysys.{Analysis, Benchmark, BenchmarkResult}
import baldwin.BaldwinMain
import func.{Func, GeneticFuncMain, HoldersTableFunction, LabTestFunction}
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.{Crossover, Generation}
import genetic.localOptima.LocalOptimaSignal
import genetic.selection.ParentSelection
import genetic.survivors.SurvivorSelectionStrategy
import genetic.survivors.construction.{DeduplicatedConstruction, NormalConstruction, PopulationConstruction}
import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticAlg, GeneticEngine, GeneticMetadata}
import knapsack.{GeneticKnapsackMain, Item}
import mdKnapsack.{MDKnapsackMain, MDKnapsackParser}
import parametric.{Instances, Parametric}
import params.{GeneticParamsMain, Params}
import queens.{GeneticQueenMain, QueenMating, QueenMutation}
import string.{GeneticStringMain, HillClimbing, StringHeuristics}
import util.{JavaUtil, Util}

import scala.annotation.tailrec
import scala.util.Try
import scalaz.std.list.listInstance

object UserMain {
  var in: Scanner = new Scanner(System.in)

  def main(args: Array[String]) {
    println()
    println("Genetic Algorithms Lab 2 by Ilan Godik & Yuval Alfassi")

    mainMenu()

    println("Press any key to exit")
    in.nextLine()
  }


  def mainMenu(): Unit = {
    println()
    println(
      """1. Genetic Algorithm
        |2. Hill Climbing - String matching
        |3. Baldwin's Effect""".stripMargin)
    // |4. Minimal Conflicts - N-Queens

    val whatToRun = readIntLoop("Please choose what you want to run: ")

    whatToRun match {
      case 1 =>
        val alg = chooseGeneticMetadata()
        menu(alg, alg.defaultGeneticAlgParametric)
      case 2 => runHillClimbing()
      case 3 =>
        val baldwin = chooseBaldwinAlg()
        val generation =
          for {
            selection <- Instances.rws
            mutation <- Instances.mutation.updateDefaults(Map.empty, Map.empty, Map("Mutation Rate" -> 0.0))
            survivorSelection <- Instances.elitism.updateDefaults(Map.empty, Map.empty, Map("Elitism Rate" -> 0.0))
          } yield new Generation(selection, mutation, Array(survivorSelection), new NormalConstruction, Array.empty)

        val engine = Instances.geneticEngine(Instances.ignoreLocalOptima, generation, generation)
        val geneticAlg = baldwin.alg(engine)
        println("## Genetic Engine: Using RWS ##")

        menu(baldwin, geneticAlg)
      // case 4 => runMinimalConflicts()
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

  def chooseGeneticMetadata(): GeneticMetadata[_] = {
    // Choose genetic Alg.
    // Choose variants
    println("\n" +
      """1. String searching
        |2. Function optimization
        |3. N-Queens
        |4. Knapsack
        |5. Multi-Dimentional Knapsack""".stripMargin)
    val algNum = readIntLoop("What problem do you want to solve? ")
    algNum match {
      case 1 => chooseStringAlg()
      case 2 => chooseFunctionAlg()
      case 3 => chooseNQueensAlg()
      case 4 => chooseKnapsackAlg()
      case 5 => chooseMDKnapsackAlg()
      case _ => chooseGeneticMetadata()
    }
  }

  def chooseMDKnapsackAlg(): GeneticMetadata[_] = {
    print("\nChoose an instance (file name): ")
    val fileName = in.next()
    in.skip("\n|\r\n")
    try {
      val filePath = Paths.get("res/samples/").resolve(fileName)
      val data = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8)
      val instance = MDKnapsackParser.parse(data)
      new MDKnapsackMain(instance)
    } catch {
      case e: IOException =>
        println("Invalid file.")
        chooseMDKnapsackAlg()
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

  def chooseBaldwinAlg(): GeneticMetadata[_] = {
    @tailrec
    def chooseBinaryString(): Array[Byte] = {
      println("\nChoose a binary string to search (blank for 20 x 0's): ")
      val secret = in.nextLine()
      if (secret.isEmpty) Array.fill(20)(0)
      else if (secret.forall(c => c == '0' || c == '1')) {
        secret.iterator.map[Byte] {
          case '0' => 0
          case '1' => 1
        }.toArray
      } else {
        chooseBinaryString()
      }
    }

    val secret = chooseBinaryString()
    new BaldwinMain(secret)
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
      println()
      println(
        """1. PMX - Partially Matched Crossover (*)
          |2. OX  - Ordered Crossover
          |3. CX  - Cycle   Crossover
        """.stripMargin)
      val matingNum = readIntWithDefault("Choose a mating algorithm (default 1): ", 1)
      matingNum match {
        case 1 => QueenMating.pmx
        case 2 => QueenMating.ox
        case 3 => QueenMating.cx
        case _ => chooseMating()
      }
    }

    val mating = chooseMating()

    def chooseMutation(): (Array[Int], Random) => Unit = {
      println()
      println(
        """1. Displacement
          |2. Exchange (*)
          |3. Insertion
          |4. Simple Inversion
          |5. Complex Inversion
          |6. Scramble
        """.stripMargin)
      val mutationNum = readIntWithDefault("Choose a mutation algorithm (default 2): ", 2)
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

  @tailrec
  def chooseParentSelection(): Parametric[ParentSelection] = {
    println("\n# Choose Parent Selection Algorithm:")
    println(
      """1. Top Selection (*)
        |2. Roulette Wheel Selection - RWS
        |3. Stochastic Universal Sampling - SUS
        |4. Ranking
        |5. Tournament
      """.stripMargin)
    readIntWithDefault("Choose a parent selection strategy (default 1): ", 1) match {
      case 1 => Instances.topSelection
      case 2 => Instances.rws
      case 3 => Instances.sus
      case 4 => Instances.ranking
      case 5 => Instances.tournament
      case _ => chooseParentSelection()
    }
  }

  def chooseSurvivorSelection(localOptimum: Boolean): Parametric[Array[SurvivorSelectionStrategy]] = {
    println("\n# Choose Survivor Selection Algorithm:")
    val default = if (!localOptimum) 1 else 2
    println("1. Elitism " + (if (!localOptimum) "(*)" else ""))
    println("2. Elitism with Random Immigrants " + (if (localOptimum) "(*)" else ""))
    readIntWithDefault(s"Choose a survivor selection strategy (default $default): ", default) match {
      case 1 => Instances.elitism.map(Array(_))
      case 2 => Parametric.map2(Instances.elitism, Instances.randomImmigrantsElitism)(Array(_, _))
      case _ => chooseSurvivorSelection(localOptimum)
    }
  }

  def chooseFitnessMappings(): Parametric[List[FitnessMapping]] = {
    def optional[A](select: Boolean, element: A): List[A] = if (select) List(element) else Nil
    @tailrec
    def go(windowing: Boolean, exponential: Boolean, sigma: Boolean, aging: Boolean, niching: Boolean): List[Parametric[FitnessMapping]] = {
      println("\n# Choose Fitness Mappings:")
      println("1. Windowing " + (if (windowing) "(*)" else ""))
      println("2. Exponential Scaling " + (if (exponential) "(*)" else ""))
      println("3. Sigma Scaling " + (if (sigma) "(*)" else ""))
      println("4. Aging " + (if (aging) "(*)" else ""))
      println("5. Niching " + (if (niching) "(*)" else ""))
      print("Enter what you want to choose (multiple selection, blank to continue): ")
      tryReadInt() match {
        case Some(1) => go(!windowing, exponential, sigma, aging, niching)
        case Some(2) => go(windowing, !exponential, sigma, aging, niching)
        case Some(3) => go(windowing, exponential, !sigma, aging, niching)
        case Some(4) => go(windowing, exponential, sigma, !aging, niching)
        case Some(5) => go(windowing, exponential, sigma, aging, !niching)
        case Some(_) => go(windowing, exponential, sigma, aging, niching)
        case None =>
          optional(windowing, Instances.windowing) ++
            optional(exponential, Instances.exponentialScaling) ++
            optional(sigma, Instances.sigmaScaling) ++
            optional(aging, Instances.aging) ++
            optional(niching, Instances.niching)
      }
    }
    val mappings: List[Parametric[FitnessMapping]] = go(windowing = false, exponential = false, sigma = false, aging = false, niching = false)
    Parametric.parametricMonad.sequence(mappings)
  }

  def choosePopulationConstruction(): PopulationConstruction = {
    print("\nDo you want to deduplicate genes? (y/n, default n): ")
    val answer = in.nextLine().trim
    answer.toLowerCase match {
      case "y" | "yes" | "1" | "true" => new DeduplicatedConstruction
      case "n" | "no" | "0" | "false" | "" => new NormalConstruction
      case _ => choosePopulationConstruction()
    }
  }

  def chooseGeneration(localOptimum: Boolean): Parametric[Generation] = {
    if (!localOptimum) println("\n### Choosing Normal Generation ###") else println("\n### Choosing Local Optimum Generation ###")

    val parentSelectionParam: Parametric[ParentSelection] = chooseParentSelection()
    val survivorSelectionParam: Parametric[Array[SurvivorSelectionStrategy]] = chooseSurvivorSelection(localOptimum)
    val populationConstruction: PopulationConstruction = choosePopulationConstruction()
    val fitnessMappingsParam: Parametric[List[FitnessMapping]] = chooseFitnessMappings()
    val mutationStrategy = if (!localOptimum) Instances.mutation else Instances.hyperMutation
    for {
      parentSelection <- parentSelectionParam
      mutationStrategy <- mutationStrategy
      survivorSelection <- survivorSelectionParam
      fitnessMappings <- fitnessMappingsParam
    } yield new Generation(parentSelection, mutationStrategy, survivorSelection, populationConstruction, fitnessMappings.toArray)
  }

  def chooseLocalOptimumSignal(): (Boolean, Parametric[LocalOptimaSignal]) = {
    println("\n# Choose Local Optima Signal:")
    println(
      """1. Ignore Local Optima
        |2. Gene Similarity Detection (*)
        |3. Fitness Similarity Detection (std. dev)
      """.stripMargin)
    readIntWithDefault("Choose a local optima signal (default 2): ", 2) match {
      case 1 => (false, Instances.ignoreLocalOptima)
      case 2 => (true, Instances.geneSimilarity)
      case 3 => (true, Instances.stdDevSimilarity)
      case _ => chooseLocalOptimumSignal()
    }
  }

  def chooseEngine(): Parametric[GeneticEngine] = {
    println("\n########## Choosing Genetic Engine ##########\n")
    val normalGeneration: Parametric[Generation] = chooseGeneration(localOptimum = false)
    val (detectLocalOptimum, localOptimumSignal) = chooseLocalOptimumSignal()
    val localOptimumGeneration =
      if (detectLocalOptimum)
        chooseGeneration(localOptimum = true)
      else
        normalGeneration
    Instances.geneticEngine(localOptimumSignal, normalGeneration, localOptimumGeneration)
  }

  def menu(geneticMeta: GeneticMetadata[_], algParams: Parametric[GeneticAlg[_]]): Params = {
    println("\n" +
      s"""|1. run     - Run ${geneticMeta.name}
          |2. params  - Change Parameters of the Genetic Algorithm
          |3. engine  - Choose the Genetic Engine Algorithms
          |4. opt     - Optimize Parameters of the Genetic Algorithm
          |5. analyse - Create a statistical report of the Genetic Algorithm
          |6. bench   - Benchmark the Genetic Algorithm
          |7. main    - Return to the main menu
       """.stripMargin)
    print("Enter your selection: ")
    val input = in.nextLine()
    input match {
      case "run" | "1" =>
        val defaultMaxTime = geneticMeta.defaultMaxTime
        val maxTime = readDoubleWithDefault(s"Enter the maximum runtime in seconds (default $defaultMaxTime): ", defaultMaxTime) max 0
        val defaultPrintEvery = geneticMeta.defaultPrintEvery
        val printEvery = readIntWithDefault(s"Print best every how many iterations? (default $defaultPrintEvery, 0 for never) ", defaultPrintEvery) max 0
        val newParams = runGenetic(geneticMeta, algParams, maxTime, printEvery)
        if (!geneticMeta.isOpt) menu(geneticMeta, algParams)
        else newParams // return, not recurse
      case "params" | "2" =>
        val newParams = modifyParams(algParams)
        menu(geneticMeta, newParams)
      case "engine" | "3" =>
        val newEngine = chooseEngine()
        menu(geneticMeta, geneticMeta.alg(newEngine))
      case "opt" | "4" =>
        val geneticParams = new GeneticParamsMain(geneticMeta, algParams, defaultMaxTime = 100.0)
        val optimizedAlgParams = menu(geneticParams, geneticParams.defaultGeneticAlgParametric).prettify
        menu(geneticMeta, algParams.updateArrayParams(optimizedAlgParams))
      case "analyse" | "5" =>
        println("Enter analysis name: ")
        val name = in.nextLine()
        analysis(name, Analysis.analysis(name, algParams))
        menu(geneticMeta, algParams)
      case "bench" | "6" =>
        bench(algParams)
        menu(geneticMeta, algParams)
      case "main" | "7" =>
        mainMenu()
        System.exit(0)
        throw new IllegalStateException()
      case _ => menu(geneticMeta, algParams)
    }
  }

  def analysis(name: String, analysisParams: Parametric[Analysis]): Unit = {
    println()
    println(
      """run    - Run the analysis
        |params - Change analysis parameters
      """.stripMargin)
    print("Enter your selection: ")
    val input = in.nextLine()
    input match {
      case "run" =>
        analysisParams.applyDefaults().runAnalysis()
      case "params" =>
        val newAnalysisParams = modifyParams(analysisParams)
        analysis(name, newAnalysisParams)
      case _ =>
        analysis(name, analysisParams)
    }
  }

  def bench(alg: Parametric[GeneticAlg[_]]): Unit = {
    val rounds = readIntWithDefault("Enter the number of rounds (1000 default): ", 1000)
    val maxTime = readDoubleWithDefault("Enter the time limit per run (0.3 default): ", 0.3)
    val benchmark = Benchmark.benchmark(alg.asInstanceOf[Parametric[GeneticAlg[Object]]], rounds, maxTime)
    println()
    println(benchmark.prettyFormat)
  }

  def modifyParams[A](params: Parametric[A]): Parametric[A] = {
    val ints = params.intNamesDefaults.toIndexedSeq
    val doubles = params.doubleNamesDefaults.toIndexedSeq
    println {
      (ints ++ doubles).iterator.zipWithIndex.map {
        case ((name, value), index) => s"${index + 1}. $name = $value"
      }.mkString("\n")
    }
    val paramNum = readIntLoop("Which parameter to change? (0 to skip)  ")
    if (paramNum == 0)
      params
    else if (paramNum >= 1 && paramNum < ints.length + 1) {
      val index = paramNum - 1
      val paramName = ints(index)._1
      val newValue = readIntLoop(s"Set $paramName = ")
      Parametric(params.applyParams, params.intNamesDefaults.updated(paramName, newValue), params.intsMin, params.intsMax, params.doubleNamesDefaults)

    } else if (paramNum < ints.length + doubles.length + 1) {
      val index = paramNum - ints.length - 1
      val paramName = doubles(index)._1
      val newValue = readDoubleLoop(s"Set $paramName = ")
      Parametric(params.applyParams, params.intNamesDefaults, params.intsMin, params.intsMax, params.doubleNamesDefaults.updated(paramName, newValue))

    } else {
      params
    }
  }

  def runGenetic(main: GeneticMetadata[_], algParams: Parametric[GeneticAlg[_]], maxTime: Double, printEvery: Int): Params = {
    val alg = algParams.applyDefaults()
    val genetic: Genetic[Object] = alg.genetic.asInstanceOf[Genetic[Object]]

    val start = System.currentTimeMillis

    val (population: Population[_], iterations) = alg.run(printEvery, maxTime)

    val end = System.currentTimeMillis
    val time = end - start

    val popSize = population.population.length
    println(s"Best ${5 min popSize}:")
    println(population.population.sortBy(_.fitness).take(5).map { gene =>
      val geneObj = gene.gene.asInstanceOf[Object]
      genetic.show(geneObj) + ", fitness = " + genetic.fitness(geneObj) + ", meaningful fitness = " + genetic.score(gene.asInstanceOf[Gene[Object]])
    }.mkString("\n"))
    println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + main.seed)
    if (main.isOpt) {
      population.asInstanceOf[Population[Params]].population(0).gene
    } else
      algParams.defaultNamedParams.toParams
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
