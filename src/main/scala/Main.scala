import scala.annotation.tailrec
import scala.collection.generic.SeqFactory
import scala.collection.immutable.HashMap
import scala.collection.immutable.HashMap.HashMap1
import scala.collection.immutable.Stream.{Cons, Empty}
import scala.io.StdIn

/**
  * The program should accept the following commands:
  * *
  * add "$title" "$author": adds a book to the library with the given title and author. All books are unread by default.
  * read "$title": marks a given book as read.
  * show all: displays all of the books in the library
  * show unread: display all of the books that are unread
  * show all by "$author": shows all of the books in the library by the given author.
  * show unread by "$author": shows the unread books in the library by the given author
  * quit: quits the program.
  */


object Main extends App {

  def evaluatedLength[T](stream: => Stream[T]): Int = {
    @tailrec
    def go(s: => Stream[T], acc: Int): Int = s match {
      case Empty => acc
      case c: Cons[T] =>
        if (c.tailDefined)
          go(c.tail, acc + 1)
        else
          acc + 1
    }
    go(stream, 0)
  }

  def streamLines: Stream[String] = {
    val line: String = StdIn.readLine()
    if (line == null) Stream.Empty
    else Stream.cons(line, streamLines)
  }.filter(_.nonEmpty)

  println("Welcome to your library!")

  //  val commandStream = Stream.continually(StdIn.readLine()).filter(_.nonEmpty).map(Command.parse)
  val commandStream = streamLines.map(Command.parse)

  import Library._

  commandStream
    .map { c =>
      c match {
        case Command("add", List(title, author)) => println(f"Added $title by $author") //add title and author to library and set to unread
        case Command("read", List(title)) => println(f"You've read $title!") //mark book with this title as read
        case Command("show", args) => args match {
          // all: displays all of the books in the library
          case "all" :: Nil => show(build(commandStream))

          // all by "$author": shows all of the books in the library by the given author.
          case "all" :: "by" :: author :: Nil => show(build(commandStream).filterAuthor(author))

          // unread: display all of the books that are unread
          case "unread" :: Nil => show(build(commandStream).unread)

          // unread by "$author": shows the unread books in the library by the given author
          case "unread" :: "by" :: author :: Nil => show(build(commandStream).unread.filterAuthor(author))

          case _ => println("Did you mean one of \"show (all|unread) [by $author]\" ?")
        }
        case Command("quit", _) => System.exit(0)
        case _ => println("BAD COMMAND OR FILE NAME")
      }
      c
    }
    .force
}

case class Library(books: Map[String, String], read: Set[String]) {
  def unread: Library = copy(books = books.filterKeys(read.contains))

  def filter(p: ((String, String)) => Boolean): Library = copy(books = books.filter(p))

  def filterAuthor(author: String): Library = filter(_._2 == author)
}

object Library {
  def apply(): Library = Library(Map(), Set())

  def build(commandStream: Stream[Command]): Library = {
    commandStream.take(Main.evaluatedLength(commandStream)).foldLeft(Library()) { (library, c) =>
      c match {
        case Command("add", List(title, author)) => library.copy(books = library.books + (title -> author))
        case Command("read", List(title)) => library.copy(read = library.read + title)
        case _ => library
      }
    }
  }

  def show(library: Library) = {
    library.books.foreach({ (title: String, author: String) =>
      println(f"$title by $author (${if (library.read(title)) "read" else "unread"})")
    }.tupled)
  }
}