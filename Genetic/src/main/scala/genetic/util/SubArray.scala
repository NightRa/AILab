package genetic.util

import scala.collection.mutable

class SubArray[A](val arr: Array[A], val start: Int, val size: Int) {
  def take(n: Int): SubArray[A] = new SubArray[A](arr, start, size min n)

  def drop(n: Int): SubArray[A] = {
    val dropped = n min size
    new SubArray[A](arr, start + dropped, size - dropped)
  }

  def apply(i: Int): A = arr(start + i)
  def update(i: Int, x: A): Unit = arr(start + i) = x

  def seq: mutable.IndexedSeqView[A, Array[A]] = new mutable.IndexedSeqView[A, Array[A]] {
    def length: Int = size
    def apply(idx: Int): A = arr(start + idx)
    def update(idx: Int, elem: A): Unit = arr(start + idx)
    def underlying: Array[A] = arr
  }
}

object SubArray {
  def apply[A](arr: Array[A]): SubArray[A] = new SubArray[A](arr, 0, arr.length)

  def unapplySeq[A](x: SubArray[A]): Option[Seq[A]] =
    Some(x.seq)

}

