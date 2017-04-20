package com.bastman.kgeojson.jackson.examples

import com.bastman.kgeojson.jackson.Crs
import com.bastman.kgeojson.jackson.FeatureCollection

fun main(args: Array<String>) {
    Main.run()
}


object Main {
    val JSON = Json.JSON.serializerExcludeNull()

    fun run() {
        toJsonExample()
        fromJsonExample()
    }

    fun toJsonExample() {
        println("==== toJsonExample ... ====")

        val featureCollection = FeatureCollection(
                crs = Crs.NamedCrs(properties = mapOf()),
                bbox = listOf(1.0, 2.0, 3.0),
                features = listOf()
        )

        println(featureCollection)
        val json = JSON.encode(featureCollection)

        println("featureCollection as json: $json")
    }

    fun fromJsonExample() {
        println("===== fromJsonExample ... =====")
        val inJson = """
{
"type":"FeatureCollection",
"crs":{
"type":"name",
"properties":{
"name": "urn:ogc:def:crs:OGC:1.3:CRS84"
}
},
"features":[{"type":"Feature","id":"ARG","properties":{"name":"Argentina"},"geometry":{"type":"LineString","coordinates":[[100,0],[101,1]],"bbox":[200,0,205,2]}}]}

"""
        val featureCollection: FeatureCollection = JSON.decode(inJson)

        println("parsed from json: $featureCollection")

        featureCollection.features.forEach {
            println("${it.javaClass} : $it")
        }


        val outJson = JSON.encode(featureCollection)

        println("encoded as json: $outJson")
    }

}

