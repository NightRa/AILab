package params

class NamedParams(val ints: Array[(String, Int)], val doubles: Array[(String, Double)]) {
  def toParams: Params = new Params(ints.map(_._2), doubles.map(_._2))
}

object NamedParams {
  def apply(ints: (String, Int)*)(doubles: (String, Double)*): NamedParams =
    new NamedParams(ints.toArray, doubles.toArray)
}
