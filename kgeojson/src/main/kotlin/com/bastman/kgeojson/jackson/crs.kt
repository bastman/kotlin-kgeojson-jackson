package com.bastman.kgeojson.jackson

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

interface CrsInterface {
    val type: String
    val properties: Map<String, Any?>
}

object CrsType {
    const val Named = "name"
    const val Linked = "link"
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes(
        JsonSubTypes.Type(value = Crs.NamedCrs::class, name = CrsType.Named),
        JsonSubTypes.Type(value = Crs.LinkedCrs::class, name = CrsType.Linked)
)
sealed class Crs() : CrsInterface {

    data class NamedCrs(
            override val properties: Map<String, Any?>
    ) : Crs() {
        override val type = CrsType.Named

        companion object {
            const val PROPERTIES_NAME = "name"
        }
    }

    data class LinkedCrs(
            override val properties: Map<String, Any?>
    ) : Crs() {
        override val type: String = CrsType.Linked

        companion object {
            const val PROPERTIES_HREF = "href"
            const val PROPERTIES_TYPE = "type"
        }
    }

}