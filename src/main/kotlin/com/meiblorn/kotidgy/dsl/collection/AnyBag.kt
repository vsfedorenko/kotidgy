package com.meiblorn.kotidgy.dsl.collection

class AnyBag<T>(collection: Collection<T>) : MutableList<T> by ArrayList(collection) {
    constructor(iterable: Iterable<T>) : this(iterable.toList())
    constructor(vararg elements: T) : this(mutableListOf(*elements))
}