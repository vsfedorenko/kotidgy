package com.meiblorn.kotidgy.engine.renderer

import com.meiblorn.kotidgy.domain.markup.template.node.Node
import com.meiblorn.kotidgy.engine.renderer.node.*
import kotlin.reflect.KClass

class DelegatingNodeRenderer : Renderer<Node, Any?> {

    private var renderers = listOf(
        AnyOfRenderer(this),
        CompositionRenderer(this),
        ConstRenderer(),
        LambdaRenderer(),
        NothingRenderer(),
        OptionalRenderer(this)
    )

    private var rendererMapping: Map<KClass<out Node>, NodeRenderer<out Node, Any?>> =
        renderers.map { Pair(it.type(), it) }.toMap()

    override fun type(): KClass<Node> {
        return Node::class
    }

    @Suppress("UNCHECKED_CAST")
    override fun process(item: Node): Any? {
        val type = item::class

        if (!rendererMapping.containsKey(type)) {
            throw RuntimeException("Incompatible node type argument is passed")
        }

        val nodeRenderer = rendererMapping[type] as NodeRenderer<Node, *>
        return nodeRenderer.process(item)
    }
}