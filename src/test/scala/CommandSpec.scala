import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by aphex on 10/2/16.
  */
class CommandSpec extends FlatSpec with Matchers {

  "Parsing" should "split input into a command and arguments" in {
    Command.parse("command arg1 arg2") shouldBe Command("command", List("arg1", "arg2"))
  }

  it should "handle one quoted argument with spaces in it" in {
    Command.parse("add \"outer space\"") shouldBe Command("add", List("outer space"))
  }

  it should "handle multiple quoted arguments with spaces in it" in {
    Command.parse("no \"mr bond\" i expect you \"to die\"") shouldBe
      Command("no", List("mr bond", "i", "expect", "you", "to die"))
  }


}
