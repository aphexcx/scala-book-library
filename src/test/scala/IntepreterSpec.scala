import java.io.ByteArrayOutputStream

import org.scalatest.{Matchers, WordSpec}

/** Interpreter Tests
  *
  * Some of these tests for command parsing could probably be moved to CommandSpec.
  */

class IntepreterSpec extends WordSpec with Matchers {

  "Interpreter" when {

    val addAndShow: Stream[String] =
      """add "The Grapes of Wrath" "John Steinbeck"
        |add "Of Mice and Men" "John Steinbeck"
        |add "Moby Dick" "Herman Melville"
        |show all """
        .stripMargin.lines.toStream

    val readBooks: Stream[String] =
      """read "Moby Dick"
        |read "Of Mice and Men"
        |show all"""
        .stripMargin.lines.toStream

    val showUnread: Stream[String] =
      "show unread"
        .lines.toStream

    val addAndShowCommands: Stream[Command] =
      Command("add", "The Grapes of Wrath" :: "John Steinbeck" :: Nil) #::
        Command("add", "Of Mice and Men" :: "John Steinbeck" :: Nil) #::
        Command("add", "Moby Dick" :: "Herman Melville" :: Nil) #::
        Command("show", "all" :: Nil) #::
        Stream.Empty

    val readBooksCommands: Stream[Command] =
      Command("read", "Moby Dick" :: Nil) #::
        Command("read", "Of Mice and Men" :: Nil) #::
        Command("show", "all" :: Nil) #::
        Stream.Empty

    val showUnreadCommand: Stream[Command] =
      Command("show", "unread" :: Nil) #::
        Stream.Empty

    "adding a few books" should {

      "interpret the right add and show commands" in {
        Interpreter.fromLines(addAndShow).interpret should
          contain theSameElementsInOrderAs addAndShowCommands
      }

      "show the books in an unread state" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(output) {
          Interpreter(addAndShowCommands).interpret
        }

        output.toString.lines.toList should contain theSameElementsInOrderAs
          """Added "The Grapes of Wrath" by John Steinbeck
            |Added "Of Mice and Men" by John Steinbeck
            |Added "Moby Dick" by Herman Melville
            |"The Grapes of Wrath" by John Steinbeck (unread)
            |"Of Mice and Men" by John Steinbeck (unread)
            |"Moby Dick" by Herman Melville (unread)"""
            .stripMargin.lines.toList
      }
    }

    "reading books" should {
      "interpret the right read commands" in {

        Interpreter.fromLines(readBooks).interpret should
          contain theSameElementsInOrderAs readBooksCommands
      }

      "show the books in an read state" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(output) {
          Interpreter(addAndShowCommands ++ readBooksCommands).interpret
        }

        output.toString.lines.toList.takeRight(5) should contain theSameElementsInOrderAs
          """You've read "Moby Dick"!
            |You've read "Of Mice and Men"!
            |"The Grapes of Wrath" by John Steinbeck (unread)
            |"Of Mice and Men" by John Steinbeck (read)
            |"Moby Dick" by Herman Melville (read)"""
            .stripMargin.lines.toList
      }
    }

    "showing unread books" should {
      "interpret the right show command" in {

        Interpreter.fromLines(showUnread).interpret should
          contain theSameElementsInOrderAs showUnreadCommand
      }

      "show the unread book" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(output) {
          Interpreter(addAndShowCommands ++ readBooksCommands ++ showUnreadCommand).interpret
        }

        output.toString.lines.toList.takeRight(1) should contain theSameElementsInOrderAs
          """"The Grapes of Wrath" by John Steinbeck (unread)"""
            .lines.toList
      }
    }

  }
}
