package com.meiblorn.kotidgy.domain.markup.template.node.processor

class Map(val consumer: (String) -> String) : Processor<String>() {

    override fun apply(item: String) = consumer(item)

}