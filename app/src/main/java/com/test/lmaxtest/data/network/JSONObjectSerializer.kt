package com.test.lmaxtest.data.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.json.JSONObject

@Serializer(forClass = JSONObject::class)
object JSONObjectSerializer : KSerializer<JSONObject> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("JSONObject", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: JSONObject) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): JSONObject {
        val jsonStr = decoder.decodeString()
        return JSONObject(jsonStr)
    }
}