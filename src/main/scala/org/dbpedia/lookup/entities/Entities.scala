package org.dbpedia.lookup.entities

/*
* NOTICE: this file has been changed by Paolo Albano, Politecnico di Bari
 */

import org.dbpedia.extraction.util.WikiUtil._
import com.typesafe.config.ConfigFactory

trait Uri   { val uri   : String }
trait Label { val label : String }

case class Redirect(uri: String) extends Uri

case class Template(uri: String) extends Uri



case class Category(uri: String) extends Uri with Label {
    val conf = ConfigFactory.load("configuration.conf")
	val label: String = wikiDecode(uri.replace("http://it.dbpedia.org/resource/Categoria:", "")
                                    .replace("http://dbpedia.org/resource/Category:", "")
                                    .replace(conf.getString("lookup.replace.category"), ""))

}

case class OntologyClass(uri: String) extends Uri with Label {
    val conf = ConfigFactory.load("configuration.conf")
    val label: String = {
        if (uri endsWith "owl#Thing") {
            "owl#Thing"
        } else {
            val s = wikiDecode(uri.replace("http://dbpedia.org/ontology/", "")
                                  .replace("http://schema.org/", "")
                                  .replace("http://it.dbpedia.org/ontology/", "")
                                    .replace(conf.getString("lookup.replace.ontology"), "")
		                    )
            s.replaceAll("([A-Z])", " $1").trim.toLowerCase
        }
        }
    }

case class Result(
    uri: String,
    description: String,
    classes: Set[OntologyClass],
    categories: Set[Category],
    templates: Set[Template],
    redirects: Set[Redirect],
    refCount: Int
) extends Uri with Label {
    val conf = ConfigFactory.load("configuration.conf")
    val label: String = wikiDecode(uri.replace("http://dbpedia.org/resource/", "")
                                        .replace("http://it.dbpedia.org/resource/", "")
                                          .replace(conf.getString("lookup.replace.label"), ""))
}
