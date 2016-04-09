package baldwin

import scala.util.Random

abstract class BaldwinBit {
  def isQuestionMark: Boolean

  def toString: String
}

case class DataBit(bit: Bit) extends BaldwinBit {
  override def isQuestionMark: Boolean = false

  override def toString: String = bit.toString
}

case object QuestionMark extends BaldwinBit {
  override def isQuestionMark: Boolean = true

  override def toString: String = "?"
}

abstract class Bit {
  def toString: String
}

case object One extends Bit {
  override def toString: String = "1"
}

case object Zero extends Bit {
  override def toString: String = "0"
}

object Bit {
  def fromBool(b: Boolean): Bit = {
    if (b)
      One
    else
      Zero
  }

  def genRandom(rnd: Random): Bit = {
    fromBool(rnd.nextBoolean())
  }
}


