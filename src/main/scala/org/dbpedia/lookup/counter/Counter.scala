package org.dbpedia.lookup.counter


import org.dbpedia.lookup.util.Logging
import java.io.{FileInputStream, InputStream, File}
import com.typesafe.config.ConfigFactory


/**
  * Created by Paolo Albano, Politecnico di Bari on 5/10/16.
  */
class Counter extends Logging{

}

object Counter extends Logging{
  def main(args: Array[String]){
    val conf = ConfigFactory.load("configuration.conf")
    logger.info("Avvio creazione file counter")
    logger.info("File wikiPage: "+args(0))
    //val wikiPageFile = new File(args(0))
    //val counterFile = new File(args(1))
    val cc = new CounterClass(args(0),args(1),conf.getString("lookup.nTriple.refCount.baseUri"))



  }

}
