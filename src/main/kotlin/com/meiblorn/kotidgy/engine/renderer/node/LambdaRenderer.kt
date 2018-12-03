package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.Lambda
import com.meiblorn.kotidgy.dsl.collection.AllBag
import com.meiblorn.kotidgy.dsl.collection.AnyBag
import kotlin.reflect.KClass


class LambdaRenderer : NodeRenderer<Lambda<*>, Any?> {

    override fun type(): KClass<Lambda<*>> {
        return Lambda::class
    }

    override fun process(item: Lambda<*>): Any? {
        val result = item.supplier()
        return when (result) {
            is AnyBag<*> -> result
            is AllBag<*> -> result
            is Iterable<*> -> AnyBag(result)
            is Unit -> ""
            else -> result
        }
    }

}