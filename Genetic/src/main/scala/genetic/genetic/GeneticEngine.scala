package genetic.genetic

import genetic.genetic.generation.Generation
import genetic.genetic.localOptima.LocalOptimaSignal

case class GeneticEngine(localOptimaSignal: LocalOptimaSignal,
                         normalGeneration: Generation,
                         localOptimaGeneration: Generation,
                         PopulationSize: Int)
