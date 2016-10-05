import java.io.ByteArrayOutputStream

import org.scalatest.{Matchers, WordSpec}

/**
  * Created by aphex on 10/4/16.
  */
class IntepreterSpec extends WordSpec with Matchers {

  "Interpreter" when {
    "adding a few books" should {

      val addAndShowCommands: Stream[Command] =
        Command("add", "The Grapes of Wrath" :: "John Steinbeck" :: Nil) #::
          Command("add", "Of Mice and Men" :: "John Steinbeck" :: Nil) #::
          Command("add", "Moby Dick" :: "Herman Melville" :: Nil) #::
          Command("show", "all" :: Nil) #::
          Stream.Empty

      "interpret the right add and show commands" in {
        val addAndShow: Stream[String] =
          """add "The Grapes of Wrath" "John Steinbeck"
            |add "Of Mice and Men" "John Steinbeck"
            |add "Moby Dick" "Herman Melville"
            |show all """
            .stripMargin.lines.toStream

        Interpreter.fromLines(addAndShow).interpret shouldBe addAndShowCommands
      }

      "show the books in an unread state" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(output) {
          Interpreter(addAndShowCommands).interpret
        }

        output.toString.lines.toList shouldBe
          """Added "The Grapes of Wrath" by John Steinbeck
            |Added "Of Mice and Men" by John Steinbeck
            |Added "Moby Dick" by Herman Melville
            |"The Grapes of Wrath" by John Steinbeck (unread)
            |"Of Mice and Men" by John Steinbeck (unread)
            |"Moby Dick" by Herman Melville (unread)""".stripMargin.lines.toList
      }
    }

  }
}
