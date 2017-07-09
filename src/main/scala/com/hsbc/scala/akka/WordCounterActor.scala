  package com.hsbc.scala.akka

import java.io.InputStream

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.hsbc.scala.akka.LineProcessorActor._
import scala.io.Source

/**
  * Created by 44028569 on 4/13/2017.
  */
case class LineProcessed(words : Integer)
case class ProcessFile()

object  WordCounterActor{
  def props(fileName : String): Props = Props(new WordCounterActor(fileName))
}
class WordCounterActor(fileName : String) extends  Actor with  ActorLogging{

  private var lineProcessedCount = 0;
  private var totalLines = 0;
  private var totalWords = 0;
  private var requester : Option[ActorRef] = None;
  //val lineProcessor = LineProcessorActor{}
  //import lineProcessor._
  override def receive: Receive = {
    case ProcessFile() => {
            requester = Some(sender)
           // val stream : InputStream = getClass.getResourceAsStream(fileName)
           // println("file stream is :"+stream)
            val fileLines = Source.fromFile(fileName).getLines()
            fileLines.foreach(line=>{
              totalLines +=1
              val lineProcessorActor: ActorRef = context.actorOf(Props[LineProcessorActor], "Line_"+(totalLines))
             // log.info("Counting words from line number: " + totalLines)
              //log.info("input string is : "+line)
              lineProcessorActor ! ProcessLine(line)
            })
    }
    case LineProcessed(count) => {

      totalWords += count
      lineProcessedCount += 1
      //log.info("In LineProcessed Message totalLines : "+totalLines+" lineProcessedCount : "+lineProcessedCount+" :totalWords : "+totalWords)
      if(lineProcessedCount == totalLines){
        //  log.info("lineProcessedCount and totalLines are equal now: "+lineProcessedCount+" "+totalLines)
          requester.map(_ ! totalWords)
      }
    }
    case _ =>
      log.info("Unknown message received")
  }

}
