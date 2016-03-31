package util

import java.lang.Math._

object Distance {
  def square (x : Double) : Double = {
    x * x
  }
  def arrayDistanceI (x : Array[Int], y : Array[Int]): Double ={
    sqrt (x.iterator.zip(y.iterator).map{case (a,b) => (a-b)*(a-b)}.sum)
  }
  def arrayDistanceD (x : Array[Double], y : Array[Double]): Double ={
     sqrt (x.iterator.zip(y.iterator).map{case (a,b) => (a-b)*(a-b)}.sum)
  }
  def euclidianDistance(x1 : Double, y1:Double, x2 : Double, y2 : Double) : Double = {
    sqrt (square(x1 - x2) + square(y1 - y2))
  }

}
