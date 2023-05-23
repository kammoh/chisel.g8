package $package$

import scala.language.implicitConversions

class IfSeq[A](val ifSeq: Seq[A], val cond: Boolean) {
  def ++(that: Seq[A]): Seq[A] = this.toSeq ++ that

  def Else(elseItems: A*): Seq[A] = {
    if (cond) ifSeq else elseItems
  }

  def toSeq = if (cond) ifSeq else Seq()
}

object IfSeq {
  implicit def ifSeqToSeq[A](ifSeq: IfSeq[A]) = ifSeq.toSeq

  def apply[A](cond: Boolean)(ifItems: A*): IfSeq[A] = {
    new IfSeq(ifItems, cond)
  }
}

package object prelude {
  implicit class BoolToInt(val b: Boolean) extends AnyVal {
    def toInt: Int = if (b) 1 else 0
    def *(x: Int): Int = if (b) x else 0
  }
}
