package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.rule.Composition
import com.meiblorn.kotidgy.dsl.collection.AllBag
import com.meiblorn.kotidgy.engine.renderer.DelegatingNodeRenderer
import kotlin.reflect.KClass


class CompositionRenderer(private val delegatingNodeRenderer: DelegatingNodeRenderer) :
    NodeRenderer<Composition, AllBag<*>> {

    override fun type(): KClass<Composition> {
        return Composition::class
    }

    override fun process(item: Composition): AllBag<*> {
        return AllBag(item.nodes.map { delegatingNodeRenderer.process(it) })
    }

}