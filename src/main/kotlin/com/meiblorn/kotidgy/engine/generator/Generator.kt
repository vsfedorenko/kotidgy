package com.meiblorn.kotidgy.engine.generator

import com.meiblorn.kotidgy.domain.generation.sample.Sample

abstract class Generator<T> {

    abstract fun process(source: T): Sequence<Sample<*>>

}