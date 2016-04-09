package baldwin

import java.util
import java.util.Random

import genetic.mating.Crossover

object BaldwinBitString {

  def bitStringEquals(a: Array[Bit], b: Array[Bit]): Boolean = {
    util.Arrays.deepEquals(a.asInstanceOf[Array[AnyRef]], b.asInstanceOf[Array[AnyRef]])
  }

  def generateBitStringRandomly(length: Int, rnd: Random): Array[BaldwinBit] = {
    Array.fill(length)(genBaldwinBit(rnd))
  }

  def genBaldwinBit(rnd: Random): BaldwinBit = {
    if (rnd.nextBoolean())
      QuestionMark
    else
      DataBit(Bit.genRandom(rnd))
  }

  def fillBitStringRandomly(array: Array[BaldwinBit], rnd: Random): Array[Bit] = {
    array.map {
      case DataBit(bit) => bit
      case QuestionMark => Bit.genRandom(rnd)
    }
  }

  def localSearchToBest(bitString: Array[BaldwinBit],
                        maxIterations: Int,
                        isFinal: Array[Bit] => Boolean,
                        rnd: Random): Int = {
    if (asBits(bitString).exists(isFinal)) {
      return maxIterations
    }
    var iteration = 0
    var bits: Array[Bit] = Array.empty
    do {
      bits = fillBitStringRandomly(bitString, rnd)
      iteration += 1
    } while (iteration < maxIterations && !isFinal(bits))
    maxIterations - iteration
  }

  def onePointCrossOver(x: Array[BaldwinBit], y: Array[BaldwinBit], rnd: Random): Array[BaldwinBit] = {
    Crossover.onePointCrossover(x, y, rnd)
  }

  def show(x: Array[BaldwinBit]): String = {
    x.mkString
  }

  def asBits(array: Array[BaldwinBit]): Option[Array[Bit]] = {
    val length = array.length
    val newArray = new Array[Bit](length)
    for (i <- 0 until length) {
      array(i) match {
        case DataBit(bit) =>
          newArray(i) = bit
        case QuestionMark =>
          return None
      }
    }
    Some(newArray)
  }
}
