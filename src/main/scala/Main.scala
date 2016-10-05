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

  def stdInStream: Stream[String] = {
    val line: String = StdIn.readLine()
    if (line == null) Stream.Empty
    else Stream.cons(line, stdInStream)
  }

  Interpreter.fromLines(stdInStream).interpret
}