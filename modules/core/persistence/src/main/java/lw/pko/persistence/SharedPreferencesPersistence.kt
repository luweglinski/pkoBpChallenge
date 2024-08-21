package lw.pko.persistence

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KClass

class SharedPreferencesPersistence(context: Context, prefName: String) : Persistence {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    override fun <T> put(key: String, value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
            apply()
        }
    }

    override fun remove(key: String) {
        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
    }

    override fun <T : Any> get(key: String, klass: KClass<T>): T? {
        return when (klass) {
            String::class -> sharedPreferences.getString(key, null)
            Int::class -> sharedPreferences.getInt(key, -1)
            Boolean::class -> sharedPreferences.getBoolean(key, false)
            Float::class -> sharedPreferences.getFloat(key, -1f)
            Long::class -> sharedPreferences.getLong(key, -1L)
            else -> throw IllegalArgumentException("Unsupported type")
        } as T?
    }

    override fun <T : Any> get(key: String, klass: KClass<T>, defaultValue: T): T {
        return when (klass) {
            String::class -> sharedPreferences.getString(key, defaultValue as String)
            Int::class -> sharedPreferences.getInt(key, defaultValue as Int)
            Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as Boolean)
            Float::class -> sharedPreferences.getFloat(key, defaultValue as Float)
            Long::class -> sharedPreferences.getLong(key, defaultValue as Long)
            else -> throw IllegalArgumentException("Unsupported type")
        } as T
    }

    override fun <T : Any> putList(key: String, list: List<T>) {
        val value = list.joinToString(",")
        put(key, value)
    }

    override fun <T : Any> getList(key: String, klass: KClass<T>): List<T> {
        return sharedPreferences.getString(key, "")?.let {
            it.split(",").mapNotNull {
                when (klass) {
                    String::class -> it
                    Int::class -> it.toIntOrNull()
                    Boolean::class -> it.toBoolean()
                    else -> null
                } as? T
            }
        } ?: emptyList()
    }
}