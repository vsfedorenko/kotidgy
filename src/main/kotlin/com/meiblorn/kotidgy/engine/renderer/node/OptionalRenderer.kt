package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.rule.Optional
import com.meiblorn.kotidgy.dsl.collection.AnyBag
import com.meiblorn.kotidgy.engine.renderer.DelegatingNodeRenderer
import kotlin.reflect.KClass


class OptionalRenderer(private val delegatingNodeRenderer: DelegatingNodeRenderer) :
    NodeRenderer<Optional, AnyBag<*>> {

    override fun type(): KClass<Optional> {
        return Optional::class
    }

    override fun process(item: Optional): AnyBag<*> {
        return AnyBag(delegatingNodeRenderer.process(item.node), null)
    }

}