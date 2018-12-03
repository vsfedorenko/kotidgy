package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.rule.AnyOf
import com.meiblorn.kotidgy.dsl.collection.AnyBag
import com.meiblorn.kotidgy.engine.renderer.DelegatingNodeRenderer
import kotlin.reflect.KClass


class AnyOfRenderer(private val delegatingNodeRenderer: DelegatingNodeRenderer) : NodeRenderer<AnyOf, AnyBag<*>> {

    override fun type(): KClass<AnyOf> {
        return AnyOf::class
    }

    override fun process(item: AnyOf): AnyBag<*> {
        return AnyBag(item.alternatives.map { delegatingNodeRenderer.process(it) })
    }

}