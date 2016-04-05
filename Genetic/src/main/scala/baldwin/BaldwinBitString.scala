package baldwin

import scala.util.Random

object BaldwinBitString  {

  def genRandomly (bitStringToFind : Array[Bit],
                   maxIterations : Int,
                   distFunc : Array[Bit] => Double,
                   rnd : Random) : Array[Bit] = {
    val rndBaldwinBit = generateBitStringRandomly(bitStringToFind, rnd)
    localSearchToBest(rndBaldwinBit, maxIterations, distFunc, rnd)
  }

  def generateBitStringRandomly(bitStringToFind : Array[Bit], rnd : Random) : Array[BaldwinBit] = {
    val length = bitStringToFind.length
    val array = new Array[BaldwinBit](length)
    for (i <- 0 until length){
      if (rnd.nextBoolean())
        array(i) = QuestionMark() // 50%
      else if (rnd.nextBoolean())
        array(i) = DataBit(One()) //  25%
      else
        array(i) = DataBit(Zero()) // 25%
    }
    array
  }

  def localSearchToBest(bitString : Array[BaldwinBit],
                        maxIterations : Int,
                        distFunc : Array[Bit] => Double,
                        rnd : Random) : Array[Bit] = {
    val length = bitString.length
    def fillBitStringRandomly(): Array[Bit] = {
      val bitArray = new Array[Bit](length)
      for (i <- 0 until length) {
        bitString(i) match {
          case QuestionMark() =>
            if (rnd.nextBoolean())
              bitArray(i) = One()
            else
              bitArray(i) = Zero()
          case DataBit(bit) =>
            bitArray(i) = bit
        }
      }
      bitArray
    }

    Stream.from(1)
      .map(_ => fillBitStringRandomly())
      .take(maxIterations)
      .minBy(a => distFunc(a))
  }

  def onePointCrossOver (x : Array[Bit], y : Array[Bit], rnd : Random) : Array[Bit] = {
    val length = x.length
    val index = rnd.nextInt(length)
    val newArray = y.clone()
    for (i <- 0 until index){
      newArray(i) = x(i)
    }
    newArray
  }

  def mutateRandomly(x: Array[Bit], rnd : Random) : Unit = {
    val length = x.length
    val index = rnd.nextInt(length)
    x(index) match{
      case One()  => x(index) = Zero()
      case Zero() => x(index) = One()
    }
  }

  def show (x : Array[Bit]) : String = {
    val str = new StringBuilder()
    for (bit <- x){
      bit match {
        case Zero() => str += '0'
        case One() => str += '1'
      }
    }
    str.toString()
  }
}
