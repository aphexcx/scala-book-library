import Library._

case class Interpreter(commandStream: Stream[Command]) {

  def interpret: Stream[Command] =
    commandStream
      .map { c =>
        c match {
          case Command("add", List(title, author)) => println(f"""Added "$title" by $author""") //add title and author to library and set to unread
          case Command("read", List(title)) => println(f"""You've read "$title!"""") //mark book with this title as read
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
          case Command("quit", _) => println("Bye!"); System.exit(0)
          case _ => println("BAD COMMAND OR FILE NAME")
        }
        c
      }
      .force
}

object Interpreter {
  def fromLines(lines: Stream[String]): Interpreter =
    new Interpreter(lines
      .map(_.trim).filter(_.nonEmpty)
      .map(Command.parse))
}