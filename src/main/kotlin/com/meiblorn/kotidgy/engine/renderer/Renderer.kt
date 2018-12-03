package com.meiblorn.kotidgy.engine.renderer

import kotlin.reflect.KClass

interface Renderer<T : Any, out R> {

    fun type(): KClass<T>

    fun process(item: T): R

}