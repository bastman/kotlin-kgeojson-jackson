package com.bastman.kgeojson.jackson

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

typealias Bbox = List<Double>
typealias Position = List<Double>
typealias PointCoordinates = Position

// For type "MultiPoint", the "coordinates" member must be an array of positions.
typealias MultiPointCoordinates = List<Position>

// For type "LineString", the "coordinates" member must be an array of two or more positions.
typealias LineStringCoordinates = List<Position>

// For type "MultiLineString", the "coordinates" member must be an array of LineString coordinate arrays.
typealias MultiLineStringCoordinates = List<LineStringCoordinates>

// The list of coordinates for Polygons is nested one more level than that for LineStrings
typealias PolygonCoordinates = List<List<Position>>
// For type "MultiPolygon", the "coordinates" member is an array of Polygon coordinate arrays.
typealias MultiPolygonCoordinates = List<PolygonCoordinates>



object GeoJsonObjectType {
    const val Point = "Point"
    const val MultiPoint = "MultiPoint"
    const val LineString = "LineString"
    const val MultiLineString = "MultiLineString"
    const val Polygon = "Polygon"
    const val MultiPolygon = "MultiPolygon"
    const val GeometryCollection = "GeometryCollection"
    const val Feature = "Feature"
    const val FeatureCollection = "FeatureCollection"
}

interface GeoJsonObjectInterface {
    val type: String
    val crs: Crs?
    val bbox: Bbox?
}


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes(
        // features
        JsonSubTypes.Type(value = Feature::class, name = GeoJsonObjectType.Feature),
        JsonSubTypes.Type(value = FeatureCollection::class, name = GeoJsonObjectType.FeatureCollection),
        // geometries
        JsonSubTypes.Type(value = GeometryCollection::class, name = GeoJsonObjectType.GeometryCollection),
        JsonSubTypes.Type(value = Point::class, name = GeoJsonObjectType.Point),
        JsonSubTypes.Type(value = MultiPoint::class, name = GeoJsonObjectType.MultiPoint),
        JsonSubTypes.Type(value = LineString::class, name = GeoJsonObjectType.LineString),
        JsonSubTypes.Type(value = MultiLineString::class, name = GeoJsonObjectType.MultiLineString),
        JsonSubTypes.Type(value = Polygon::class, name = GeoJsonObjectType.Polygon),
        JsonSubTypes.Type(value = MultiPolygon::class, name = GeoJsonObjectType.MultiPolygon)
)
sealed class GeoJsonObject() : GeoJsonObjectInterface

// feature
data class FeatureCollection(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val features: List<Feature>
) : GeoJsonObject() {
    override val type = GeoJsonObjectType.FeatureCollection
}

data class Feature(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val id: String?,
        val properties: Map<String, Any?>?,
        val geometry: Geometry
) : GeoJsonObject() {
    override val type = GeoJsonObjectType.Feature
}


// geometry
data class GeometryCollection(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val geometries: List<Geometry>
) : GeoJsonObject() {
    override val type = GeoJsonObjectType.GeometryCollection
}


sealed class Geometry : GeoJsonObject()
data class Point(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: PointCoordinates
) : Geometry() {
    override val type = GeoJsonObjectType.Point
}

data class MultiPoint(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: MultiPointCoordinates
) : Geometry() {
    override val type = GeoJsonObjectType.MultiPoint
}

data class LineString(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: LineStringCoordinates

) : Geometry() {
    override val type = GeoJsonObjectType.LineString
}

data class MultiLineString(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: MultiLineStringCoordinates
) : Geometry() {
    override val type = GeoJsonObjectType.MultiLineString
}

data class Polygon(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: PolygonCoordinates
) : Geometry() {
    override val type = GeoJsonObjectType.Polygon
}

data class MultiPolygon(
        override val crs: Crs?,
        override val bbox: Bbox?,
        val coordinates: MultiPolygonCoordinates
) : Geometry() {
    override val type = GeoJsonObjectType.MultiPolygon
}







