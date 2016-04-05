package baldwin

abstract class BaldwinBit
case class DataBit(bit : Bit) extends BaldwinBit
case class QuestionMark() extends BaldwinBit

abstract class Bit
case class One() extends Bit
case class Zero() extends Bit


