package genetic.analysys

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.function.IntFunction

import genetic.mdKnapsack.{MDKnapsackInstance, MDKnapsackMain, MDKnapsackParser}

object MDKnapsackAnalysis extends App {
  val prettyOutFolder = Paths.get("MDKnapsack-Analysis/pretty/")
  val csvOutFolder = Paths.get("MDKnapsack-Analysis/")
  val csvOutFile = csvOutFolder.resolve("analysis.csv")

  Files.createDirectories(prettyOutFolder)
  Files.createDirectories(csvOutFolder)

  Files.write(csvOutFile, (Benchmark.csvHeading + "\n").getBytes)

  val filesInFolder: Array[Path] = Files.list(Paths.get("Genetic/res/samples/")).toArray(new IntFunction[Array[Path]] {
    override def apply(size: Int): Array[Path] = new Array[Path](size)
  })
  val sampleFiles = filesInFolder.filter(_.getFileName.toString.endsWith(".DAT"))
  val instances = sampleFiles.map(file => (file.getFileName.toString.dropRight(4).capitalize, readSample(file)))
  for ((problemName, instance) <- instances) {
    println(s"## Starting Problem: $problemName")
    val main = new MDKnapsackMain(instance)
    val benchmark = Benchmark.benchmark(main.defaultGeneticAlgParametric, 100, 1)
    Files.write(csvOutFile, (benchmark.csvFormat(problemName) + "\n").getBytes, StandardOpenOption.APPEND)
    Files.write(prettyOutFolder.resolve(s"$problemName.txt"), benchmark.prettyFormat.getBytes)
    println(benchmark.prettyFormat)
  }

  def readSample(path: Path): MDKnapsackInstance = {
    val data = new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
    MDKnapsackParser.parse(data)
  }



}
