import scala.annotation.tailrec
import scala.collection.immutable.Stream.{Cons, Empty}
import scala.language.implicitConversions

class RichStream[T](stream: Stream[T]) {

  def evaluatedLength: Int = {
    @tailrec
    def go(s: Stream[T], acc: Int): Int = s match {
      case Empty => acc
      case c: Cons[T] =>
        if (c.tailDefined)
          go(c.tail, acc + 1)
        else
          acc + 1
    }
    go(stream, 0)
  }

  def takeEvaluated: Stream[T] = stream.take(evaluatedLength)
}

object RichStream {
  implicit def enrichStream[T](stream: Stream[T]): RichStream[T] = new RichStream(stream)
}
