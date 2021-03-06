package com.leobenkel.zparkioProjectExample

import com.leobenkel.zparkio.Services._
import com.leobenkel.zparkio.ZparkioApp
import zio.{Task, UIO, ZIO}

trait Application extends ZparkioApp[Arguments, RuntimeEnv, String] {
  override def runApp(): ZIO[RuntimeEnv, Throwable, String] = {
    for {
      s     <- UIO("hello")
      _     <- Logger.info(s"Got: $s")
      a     <- Arguments(_.inputId())
      spark <- SparkModule()
      df    <- Task(spark.sparkContext.parallelize((0 until a).toSeq))
      _     <- Logger.info(s"Count: ${df.count()}")
    } yield { s }
  }

  override def processErrors(f: Throwable): Option[Int] = {
    println(f)
    f.printStackTrace()
    Some(1)
  }

  override def makeEnvironment(
    cliService:   Arguments,
    sparkService: SparkModule.Service
  ): RuntimeEnv = {
    RuntimeEnv(cliService, sparkService)
  }

  override def makeSparkBuilder: SparkModule.Builder[Arguments] = SparkBuilder

  override def makeCliBuilder: CommandLineArguments.Builder[Arguments] =
    new CommandLineArguments.Builder[Arguments] {
      override protected def createCli(args: List[String]): Arguments = {
        Arguments(args)
      }
    }
}
