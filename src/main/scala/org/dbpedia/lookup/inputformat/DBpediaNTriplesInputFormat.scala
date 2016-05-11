package org.dbpedia.lookup.inputformat

import org.semanticweb.yars.nx.parser.NxParser
import java.io.InputStream
import org.dbpedia.lookup.lucene.LuceneConfig
import com.typesafe.config.ConfigFactory

/**
 * Class to itereate over DBpedia NTriples dataset and
  *
  *
  * NOTICE: this file has been changed by Paolo Albano, Politecnico di Bari
 */
class DBpediaNTriplesInputFormat(val dataSet: InputStream, val redirects: scala.collection.Set[String]) extends InputFormat {

    private val it = new NxParser(dataSet)

    val conf = ConfigFactory.load("configuration.conf")

    val predicate2field = Map(
        //"http://lexvo.org/ontology#label" -> LuceneConfig.Fields.SURFACE_FORM_KEYWORD,   // no DBpedia dataset, has to be created
        conf.getString("lookup.nTriple.label.baseUri") -> LuceneConfig.Fields.SURFACE_FORM_KEYWORD,   // no DBpedia dataset, has to be created
        conf.getString("lookup.nTriple.refCount.baseUri") -> LuceneConfig.Fields.REFCOUNT,  // no DBpedia dataset, has to be created
        conf.getString("lookup.nTriple.description.baseUri") -> LuceneConfig.Fields.DESCRIPTION,
        //conf.getString("lookup.label.baseUri") -> LuceneConfig.Fields.DESCRIPTION,
        conf.getString("lookup.nTriple.class.baseUri") -> LuceneConfig.Fields.CLASS,
        conf.getString("lookup.nTriple.category.baseUri") -> LuceneConfig.Fields.CATEGORY,
        conf.getString("lookup.nTriple.template.baseUri") -> LuceneConfig.Fields.TEMPLATE,  // not really necessary
        conf.getString("lookup.nTriple.redirect.baseUri") -> LuceneConfig.Fields.REDIRECT      // not really necessary
    )

    override def foreach[U](f: ((String,String,String)) => U) {

        while(it.hasNext) {
            val triple = it.next
            val uri = triple(0).toString
            val pred = triple(1).toString
            val obj = triple(2).toString

            predicate2field.get(pred) match {
                case Some(field: String) if(redirects.isEmpty || !redirects.contains(uri)) => {
                    if(field == LuceneConfig.Fields.REDIRECT) {
                        f( (obj, field, uri) )   // make it a "hasRedirect" relation
                    }
                    else {
                        f( (uri, field, obj) )
                    }
                }
                case _ =>
            }
        }

    }

}