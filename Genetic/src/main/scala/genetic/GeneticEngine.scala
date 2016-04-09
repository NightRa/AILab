package genetic

import genetic.generation.Generation
import genetic.localOptima.LocalOptimaSignal

case class GeneticEngine(localOptimaSignal: LocalOptimaSignal,
                            normalGeneration: Generation,
                            localOptimaGeneration: Generation,
                            PopulationSize: Int)
