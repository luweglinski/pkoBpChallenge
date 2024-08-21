package lw.pko.persistence

import kotlin.reflect.KClass

interface Persistence {
    fun <T> put(key: String, value: T)

    fun remove(key: String)

    fun <T : Any> get(key: String, klass: KClass<T>): T?

    fun <T : Any> get(key: String, klass: KClass<T>, defaultValue: T): T

    fun <T : Any> putList(key: String,list: List<T>)

    fun <T : Any> getList(key: String, klass: KClass<T>): List<T>
}