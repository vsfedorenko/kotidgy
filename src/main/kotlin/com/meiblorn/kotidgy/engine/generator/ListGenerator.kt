package com.meiblorn.kotidgy.engine.generator

import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.dsl.collection.AnyBag

class ListGenerator(private val delegatingGenerator: DelegatingGenerator) : Generator<List<*>>() {

    override fun process(source: List<*>): Sequence<Sample<*>> = sequence {
        yieldAll(process(AnyBag(source)))
    }

}