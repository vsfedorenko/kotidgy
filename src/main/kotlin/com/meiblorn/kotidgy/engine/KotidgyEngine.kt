package com.meiblorn.kotidgy.engine

import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.domain.markup.Project
import com.meiblorn.kotidgy.engine.generator.DelegatingGenerator
import com.meiblorn.kotidgy.engine.renderer.TemplateRenderer

class KotidgyEngine(
    private val templateRenderer: TemplateRenderer = TemplateRenderer(),
    private val generator: DelegatingGenerator = DelegatingGenerator()
) {
    fun generate(project: Project): Sequence<Sample<*>> = sequence {
        val templates = project.templates
        templates.forEach {
            val renderedTemplate = templateRenderer.process(it)
            yieldAll(generator.process(renderedTemplate))
        }
    }
}