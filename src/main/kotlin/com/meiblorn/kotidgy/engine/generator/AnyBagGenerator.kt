package com.meiblorn.kotidgy.engine.generator

import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.domain.generation.sample.id.IndexId
import com.meiblorn.kotidgy.dsl.collection.AnyBag

class AnyBagGenerator(private val delegatingGenerator: DelegatingGenerator) : Generator<AnyBag<*>>() {

    override fun process(source: AnyBag<*>): Sequence<Sample<*>> = sequence {
        source.forEachIndexed { index, item ->
            delegatingGenerator.process(item).forEachIndexed { _, subItem ->
                yield(Sample(IndexId(index), subItem.toString()))
            }
        }
    }

}