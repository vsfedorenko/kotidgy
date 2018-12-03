package com.meiblorn.kotidgy.domain.markup.template.node.processor

abstract class Processor<T> {

    abstract fun apply(item: String): T

}