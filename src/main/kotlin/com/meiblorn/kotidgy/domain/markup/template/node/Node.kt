package com.meiblorn.kotidgy.domain.markup.template.node

import com.meiblorn.kotidgy.domain.markup.template.node.processor.Processor

abstract class Node {
    val processors: MutableList<Processor<*>> = mutableListOf()
}