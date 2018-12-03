package com.meiblorn.kotidgy.engine.renderer

import com.meiblorn.kotidgy.domain.markup.template.Template
import com.meiblorn.kotidgy.domain.markup.template.node.Node
import kotlin.reflect.KClass

class TemplateRenderer(private val delegatingNodeRenderer: Renderer<Node, Any?> = DelegatingNodeRenderer()) :
    Renderer<Template, Any?> {

    override fun type(): KClass<Template> {
        return Template::class
    }

    override fun process(item: Template): Any? {
        return delegatingNodeRenderer.process(item.node)
    }

}