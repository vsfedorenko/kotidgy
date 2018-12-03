package com.meiblorn.kotidgy.domain.markup.template.node.processor

class Filter(val consumer: (String) -> Boolean) : Processor<Boolean>() {

    override fun apply(item: String): Boolean = consumer(item)

}