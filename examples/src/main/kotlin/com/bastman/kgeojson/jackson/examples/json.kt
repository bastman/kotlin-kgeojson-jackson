package com.bastman.kgeojson.jackson.examples

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.io.IOException
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

class Json(val mapper: ObjectMapper) {

    companion object {

        val MAPPER_DEFAULT: ObjectMapper = jacksonObjectMapper()
                .registerModule(ParameterNamesModule())
                .registerModule(Jdk8Module())
                .registerModule(JavaTimeModule())
                // encoder
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                // decoder
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)

        val JSON_STRICT = Json(mapper = MAPPER_DEFAULT)
        val JSON = Json(mapper = MAPPER_DEFAULT).graceful()
    }

    fun withMapper(mapper: ObjectMapper): Json {
        if (mapper == this.mapper) {
            return this
        }

        return Json(mapper = mapper)
    }

    fun graceful(): Json {
        return withMapper(
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        )
    }

    fun encode(data: Any?): String = mapper.writeValueAsString(data)
    inline fun <reified T> decode(json: String): T = mapper.readValue(json, object : TypeReference<T>() {})
    fun <T> decode(json: String, valueType: Class<T>): T = mapper.readValue(json, valueType)

    inline fun <reified T> load(path: Path): T {
        try {
            val json = String(Files.readAllBytes(path))

            return decode(json)
        } catch(e: IOException) {
            throw RuntimeException(
                    "Failed to load json from path=$path reason: ${e.message}"
            )
        }
    }

    inline fun <reified T> load(uri: URI): T {
        try {
            val json = uri.toURL().readText(Charsets.UTF_8)

            return decode(json)
        } catch(e: IOException) {

            throw RuntimeException(
                    "Failed to load json from uri=" + uri + " reason: ${e.message}"
            )
        }
    }

    inline fun <reified T> load(loader: ClassLoader, resource: String): T {
        try {
            val url: URL = loader.getResource(resource)
                    ?: throw RuntimeException("IO ERROR - resource not found!")

            val json = url.readText(Charsets.UTF_8)

            return decode(json)
        } catch (all: Exception) {

            throw RuntimeException(
                    "Failed to load json from resource=$resource reason: ${all.message}"
            )
        }
    }

    fun loadText(loader: ClassLoader, resource: String): String {
        try {
            val url: URL = loader.getResource(resource)
                    ?: throw RuntimeException("IO ERROR - resource not found!")

            val text = url.readText(Charsets.UTF_8)

            return text
        } catch (all: Exception) {

            throw RuntimeException(
                    "Failed to load json from resource=$resource reason: ${all.message}"
            )
        }
    }

    fun serializerExcludeNull(): Json {
        return withMapper(
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        )
    }

}