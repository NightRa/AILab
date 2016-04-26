package genetic.params

case class NamedParams(ints: Array[(String, Int)], doubles: Array[(String, Double)]) {
  def toParams: Params = new Params(ints.map(_._2), doubles.map(_._2))

  override def toString: String = s"Params: (ints: ${ints.toMap.toString.drop(3)}, doubles: ${doubles.toMap.toString.drop(3)})"
}
