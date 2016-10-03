case class Command(name: String, args: List[String])

object Command {
  def parse(line: String): Command = {
    val tokenList: List[String] = (line :+ ' ').foldLeft(("", List[String]())) {
      (tuple, char) =>
        val (word, words) = tuple
        char match {
          case ' ' if word.isEmpty => (word, words)
          case ' ' if !word.startsWith("\"") => ("", words :+ word)
          case '"' if word.startsWith("\"") => ("", words :+ word.tail)
          case _ => (word :+ char, words)
        }
    }._2

    Command(tokenList.head, tokenList.tail)
  }
}
