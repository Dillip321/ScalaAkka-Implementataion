package com.hsbc.scala.akka

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import com.hsbc.scala.akka.WordCounterActor._

case class ProcessLine(string :String);

object  LineProcessorActor{
 def props() : Props =  Props(new LineProcessorActor())
}

class  LineProcessorActor extends Actor with ActorLogging {

  //val wordCounter = WordCounter{}
//  import  wordCounter._
  override def receive: Receive = {
    case ProcessLine(line) => {
      //log.info("In LineProcessorActor input line is: "+line)
      val words = line.split(" ").length;
      //log.info("Number of words in line :"+line+" are "+words)
      sender() ! LineProcessed(words)
    }
    case _ =>{
      log.info("Unknown message received...");
    }
  }
}