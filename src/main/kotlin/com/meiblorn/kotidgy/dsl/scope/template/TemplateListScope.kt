package com.meiblorn.kotidgy.dsl.scope.template

import com.meiblorn.kotidgy.domain.markup.template.Template
import com.meiblorn.kotidgy.domain.markup.template.node.Node

class TemplateListScope : MutableList<Template> by mutableListOf() {

    inline fun <reified T : Node> t(noinline nodeBlock: TemplateScope.() -> T) {
        return template(nodeBlock)
    }

    inline fun <reified T : Node> template(noinline nodeBlock: TemplateScope.() -> T) {
        val templateScope = TemplateScope()
        this.add(Template(templateScope.nodeBlock()))
    }

}