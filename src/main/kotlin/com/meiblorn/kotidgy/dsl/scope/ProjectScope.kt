package com.meiblorn.kotidgy.dsl.scope

import com.meiblorn.kotidgy.domain.markup.template.Template
import com.meiblorn.kotidgy.dsl.scope.template.TemplateListScope

class ProjectScope(var templates: MutableList<Template> = mutableListOf()) : Scope() {

    fun templates(init: TemplateListScope.() -> Unit) {
        val scope = TemplateListScope()
        scope.init()
        this.templates.addAll(scope)
    }

}