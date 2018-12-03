package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.Const
import kotlin.reflect.KClass


class ConstRenderer : NodeRenderer<Const, String> {

    override fun type(): KClass<Const> {
        return Const::class
    }

    override fun process(item: Const) = item.value

}