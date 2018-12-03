package com.meiblorn.kotidgy.engine.generator

import com.meiblorn.kotidgy.domain.generation.sample.NullSample
import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.domain.generation.sample.id.IndexId
import com.meiblorn.kotidgy.dsl.collection.AllBag
import com.meiblorn.kotidgy.dsl.collection.AnyBag

class DelegatingGenerator : Generator<Any?>() {

    private val allBagGenerator = AllBagGenerator(this)

    private val anyBagGenerator = AnyBagGenerator(this)

    private val listGenerator = ListGenerator(this)

    override fun process(source: Any?): Sequence<Sample<*>> = sequence {
        when (source) {
            is Collection<*> -> when {
                source.isEmpty() -> yield(NullSample)
                source is AllBag<*> -> yieldAll(allBagGenerator.process(source))
                source is AnyBag<*> -> yieldAll(anyBagGenerator.process(source))
                source is List<*> -> yieldAll(listGenerator.process(source))
            }
            else -> yield(Sample(IndexId(0), source.toString()))
        }
    }

}