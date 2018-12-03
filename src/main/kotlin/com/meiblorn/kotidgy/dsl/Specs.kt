package com.meiblorn.kotidgy.dsl

import com.meiblorn.kotidgy.domain.markup.Project
import com.meiblorn.kotidgy.dsl.scope.ProjectScope

fun kotidgy(init: ProjectScope.() -> Unit): Project {
    return project(init)
}

fun project(init: ProjectScope.() -> Unit): Project {
    val scope = ProjectScope()
    scope.init()
    return Project(scope)
}
