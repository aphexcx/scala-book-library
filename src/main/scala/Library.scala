import RichStream._

case class Library(books: Map[String, String], read: Set[String]) {
  def unread: Library = copy(books = books.filterKeys(!read.contains(_)))

  def filterAuthor(author: String): Library = filter(_._2 == author)

  def filter(p: ((String, String)) => Boolean): Library = copy(books = books.filter(p))
}

object Library {
  def apply(): Library = Library(Map(), Set())

  def build(commandStream: Stream[Command]): Library =
    commandStream.takeEvaluated.foldLeft(Library()) { (library, c) =>
      c match {
        case Command("add", List(title, author)) => library.copy(books = library.books + (title -> author))
        case Command("read", List(title)) => library.copy(read = library.read + title)
        case _ => library
      }
    }

  def show(library: Library) =
    library.books.foreach({ (title: String, author: String) =>
      println(f""""$title" by $author (${if (library.read(title)) "read" else "unread"})""")
    }.tupled)

}
