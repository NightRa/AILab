package params

case class NamedParams(ints: Array[(String, Int)], doubles: Array[(String, Double)]) {
  def toParams: Params = new Params(ints.map(_._2), doubles.map(_._2))
}

object NamedParams {
  def apply(ints: (String, Int)*)(doubles: (String, Double)*): NamedParams =
    new NamedParams(ints.toArray, doubles.toArray)
}
