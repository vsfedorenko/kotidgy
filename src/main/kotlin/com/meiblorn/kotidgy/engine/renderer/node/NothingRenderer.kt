package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.rule.Nothing
import kotlin.reflect.KClass


class NothingRenderer : NodeRenderer<Nothing, Any?> {

    override fun type(): KClass<Nothing> {
        return Nothing::class
    }

    override fun process(item: Nothing): Any? {
        return null
    }

}