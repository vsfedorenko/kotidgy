package com.meiblorn.kotidgy.domain.markup

import com.meiblorn.kotidgy.domain.markup.template.Template
import com.meiblorn.kotidgy.dsl.scope.ProjectScope

class Project(val templates: List<Template>) : Entity() {

    constructor(scope: ProjectScope) : this(scope.templates)

}