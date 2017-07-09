package com.hsbc.scala.akka

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.dispatch.ExecutionContexts._


/**
  * Created by 44028569 on 4/14/2017.
  */
object FileProcessor extends App{


  override def main(args: Array[String]): Unit = {
    implicit val ec = global
    println("Starting File Processing")
    val system = ActorSystem("FileProcessor")
    val wordCounterActor: ActorRef = system.actorOf(Props(new WordCounterActor("C:\\mahendra\\Projects\\AkkaWordCounter\\src\\main\\resources\\Data.txt")))
    implicit val timeout = Timeout(25 seconds)
    val future = wordCounterActor ? ProcessFile()
  //  println("Returned future is : "+future)
   //future.foreach(count => println(count))
    future.map { result =>
      println("Total number of words in File are : "+ result)
      system.terminate()
    }
  }
}
