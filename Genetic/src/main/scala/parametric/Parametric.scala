package parametric

import params.{NamedParams, Params}

import scala.collection.immutable.SortedMap
import scalaz.Monad

case class Parametric[+A](applyParams: (Map[String, Int], Map[String, Double]) => A,
                          intNamesDefaults: SortedMap[String, Int],
                          intsMin: SortedMap[String, Int],
                          intsMax: SortedMap[String, Int],
                          doubleNamesDefaults: SortedMap[String, Double]) {
  def map[B](f: A => B): Parametric[B] =
    Parametric[B]((ints, doubles) => f(applyParams(ints, doubles)), intNamesDefaults, intsMin, intsMax, doubleNamesDefaults)

  // It can't be a monad, just for syntax.
  def flatMap[B](f: A => Parametric[B]): Parametric[B] = {
    Parametric.join(map(f))
  }

  // Takes a 1-1 function going to a codomain disjoint from the domain.
  def scope(addSub: String => String, stripSub: String => String): Parametric[A] =
    Parametric((ints, doubles) =>
      applyParams(
        ints.map { case (k, v) => (stripSub(k), v) },
        doubles.map { case (k, v) => (stripSub(k), v) }),
      intNamesDefaults.map { case (k, v) => (addSub(k), v) },
      intsMin.map { case (k, v) => (addSub(k), v) },
      intsMax.map { case (k, v) => (addSub(k), v) },
      doubleNamesDefaults.map { case (k, v) => (addSub(k), v) })

  def prefixed(prefix: String): Parametric[A] =
    scope(prefix + _, _.drop(prefix.length))

  def applyDefaults(): A = applyParams(intNamesDefaults, doubleNamesDefaults)

  def applyArrayParams(p: Params): A = {
    val ints = intNamesDefaults.keysIterator.zip(p.ints.iterator).toMap
    val doubles = doubleNamesDefaults.keysIterator.zip(p.doubles.iterator).toMap
    applyParams(ints, doubles)
  }

  def updateArrayParams(p: Params): Parametric[A] = {
    val ints = SortedMap[String, Int](intNamesDefaults.keysIterator.zip(p.ints.iterator).toArray: _*)
    val doubles = SortedMap(doubleNamesDefaults.keysIterator.zip(p.doubles.iterator).toArray: _*)
    Parametric(applyParams, ints, intsMin, intsMax, doubles)
  }

  def updateDefaults(intNamesDefaults: Map[String, Int],
                     intsNamesMax: Map[String, Int],
                     doubleNamesDefaults: Map[String, Double]): Parametric[A] = {
    assert(intNamesDefaults.keySet.subsetOf(this.intNamesDefaults.keySet), s"Invalid default Int keys: ${intNamesDefaults.keySet} not <= ${this.intNamesDefaults.keySet}")
    assert(intsNamesMax.keySet.subsetOf(this.intsMax.keySet), s"Invalid Int-max keys: ${intsNamesMax.keySet} not <= ${this.intsMax.keySet}")
    assert(doubleNamesDefaults.keySet.subsetOf(this.doubleNamesDefaults.keySet), s"Invalid Double keys: ${doubleNamesDefaults.keySet} not <= ${this.doubleNamesDefaults.keySet}")
    Parametric(this.applyParams, this.intNamesDefaults ++ intNamesDefaults, intsMin, this.intsMax ++ intsNamesMax, this.doubleNamesDefaults ++ doubleNamesDefaults)
  }

  def defaultNamedParams: NamedParams = NamedParams(intNamesDefaults.toArray, doubleNamesDefaults.toArray)

  override def toString: String = s"Params: (ints: ${intNamesDefaults.toString.drop(3)}, doubles: ${doubleNamesDefaults.toString.drop(3)})"
}

object Parametric {
  implicit def parametricMonad: Monad[Parametric] = new Monad[Parametric] {
    override def point[A](a: => A): Parametric[A] =
      Parametric.point(a)

    override def bind[A, B](parametric: Parametric[A])(f: A => Parametric[B]): Parametric[B] =
      parametric.flatMap(f)
  }

  def point[A](a: => A): Parametric[A] =
    Parametric((ints, doubles) => a, SortedMap.empty, SortedMap.empty, SortedMap.empty, SortedMap.empty)

  def map2[A, B, C](p1: Parametric[A], p2: Parametric[B], f: (A, B) => C): Parametric[C] = {
    Parametric((ints, doubles) => f(p1.applyParams(ints, doubles), p2.applyParams(ints, doubles)), p1.intNamesDefaults ++ p2.intNamesDefaults, p1.intsMin ++ p2.intsMin, p1.intsMax ++ p2.intsMax, p1.doubleNamesDefaults ++ p2.doubleNamesDefaults)
  }

  def join[A](p1: Parametric[Parametric[A]]): Parametric[A] = {
    // Assumes the params don't depend on each other, i.e. can't be a real monad, and it isn't; it's just an applicative.
    // dummyInnerParametric
    val p2 = p1.applyParams(p1.intNamesDefaults, p1.doubleNamesDefaults)
    Parametric((ints, doubles) => p1.applyParams(ints, doubles).applyParams(ints, doubles), p1.intNamesDefaults ++ p2.intNamesDefaults, p1.intsMin ++ p2.intsMin, p1.intsMax ++ p2.intsMax, p1.doubleNamesDefaults ++ p2.doubleNamesDefaults)
  }

  def intParam(name: String, default: Int, minValue:Int, maxValue: Int): Parametric[Int] =
    Parametric((ints, doubles) => ints.getOrElse(name, throw new IllegalArgumentException(s"Int parameter not found: $name")),
      SortedMap(name -> default),
      SortedMap(name -> minValue),
      SortedMap(name -> maxValue),
      SortedMap.empty)

  def doubleParam(name: String, default: Double): Parametric[Double] =
    Parametric((ints, doubles) => doubles.getOrElse(name, throw new IllegalArgumentException(s"Double parameter not found: $name")),
      SortedMap.empty,
      SortedMap.empty,
      SortedMap.empty,
      SortedMap(name -> default))

}
