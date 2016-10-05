import RichStream._

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

  println("Welcome to your library!")

  def streamLines: Stream[String] = {
    val line: String = StdIn.readLine()
    if (line == null) Stream.Empty
    else Stream.cons(line, streamLines)
  }.filter(_.nonEmpty)

  import Library._

  val commandStream = streamLines.map(Command.parse)

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

  def filterAuthor(author: String): Library = filter(_._2 == author)

  def filter(p: ((String, String)) => Boolean): Library = copy(books = books.filter(p))
}

object Library {
  def apply(): Library = Library(Map(), Set())

  def build(commandStream: Stream[Command]): Library = {
    commandStream.takeEvaluated.foldLeft(Library()) { (library, c) =>
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