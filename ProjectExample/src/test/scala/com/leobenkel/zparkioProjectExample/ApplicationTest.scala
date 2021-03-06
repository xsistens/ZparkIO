package com.leobenkel.zparkioProjectExample

import com.leobenkel.zparkiotest.TestWithSpark
import org.scalatest.FreeSpec
import zio.Exit.{Failure, Success}
import zio.ZIO

class ApplicationTest extends FreeSpec with TestWithSpark {
  "Full application" - {
    "Run" in {
      TestApp.unsafeRunSync(TestApp.runTest("--spark-foo" :: "abc" :: Nil)) match {
        case Success(value) =>
          println(s"Read: $value")
          assertResult(0)(value)
        case Failure(cause) => fail(cause.prettyPrint)
      }
    }

    "Help" in {
      TestApp.unsafeRunSync(TestApp.runTest("--help" :: Nil)) match {
        case Success(value) =>
          println(s"Read: $value")
          assertResult(0)(value)
        case Failure(cause) => fail(cause.prettyPrint)
      }
    }
  }
}

object TestApp extends Application {
  def runTest(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    super.run(args)
  }
}
