package com.meiblorn.kotidgy.engine.generator

import com.meiblorn.kotidgy.domain.generation.sample.NullSample
import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.domain.generation.sample.id.IndexId
import com.meiblorn.kotidgy.dsl.collection.AllBag

class AllBagGenerator(private val delegatingGenerator: DelegatingGenerator) : Generator<AllBag<*>>() {

    override fun process(source: AllBag<*>): Sequence<Sample<*>> = sequence {
        val currentItem = source.first()
        for (forwardItem in delegatingGenerator.process(AllBag(source.drop(1)))) {
            val forwardItemOrNull = when {
                forwardItem != NullSample -> forwardItem
                else -> null
            }

            when {
                currentItem is List<*> -> {
                    delegatingGenerator.process(currentItem).forEachIndexed { index, currentSubItem ->
                        yield(
                            Sample(
                                IndexId(index),
                                currentSubItem.toString(),
                                forwardItemOrNull
                            )
                        )
                    }
                }
                currentItem != null -> yield(
                    Sample(
                        IndexId(0),
                        currentItem.toString(),
                        forwardItemOrNull
                    )
                )
                else -> yield(Sample(IndexId(0), "", forwardItemOrNull))
            }
        }
    }

}