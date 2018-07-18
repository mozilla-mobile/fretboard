package mozilla.components.service.fretboard

import org.json.JSONArray
import org.json.JSONObject
import java.util.TreeMap

@Suppress("UNCHECKED_CAST")
internal fun <T> JSONArray?.toList(): List<T> {
    if (this != null) {
        val result = ArrayList<T>()
        for (i in 0 until length())
            result.add(get(i) as T)
        return result
    }
    return listOf()
}

internal fun JSONObject.tryGetString(key: String): String? {
    if (!isNull(key)) {
        return getString(key)
    }
    return null
}

internal fun JSONObject.tryGetInt(key: String): Int? {
    if (!isNull(key)) {
        return getInt(key)
    }
    return null
}

internal fun JSONObject.tryGetLong(key: String): Long? {
    if (!isNull(key)) {
        return getLong(key)
    }
    return null
}

internal fun JSONObject.putIfNotNull(key: String, value: Any?) {
    if (value != null) {
        put(key, value)
    }
}

internal fun JSONObject.sortKeys(): JSONObject {
    val map = TreeMap<String, Any>()
    for (key in this.keys()) {
        map[key] = this[key]
    }
    val jsonObject = JSONObject()
    for (key in map.keys) {
        if (map[key] is JSONObject) {
            map[key] = (map[key] as JSONObject).sortKeys()
        }
        jsonObject.put(key, map[key])
    }
    return jsonObject
}

internal fun <T> List<T>.toJsonArray(): JSONArray {
    return fold(JSONArray()) { jsonArray, element -> jsonArray.put(element) }
}
