package com.meiblorn.kotidgy.dsl.scope.template

import com.meiblorn.kotidgy.domain.markup.template.node.Const
import com.meiblorn.kotidgy.domain.markup.template.node.Lambda
import com.meiblorn.kotidgy.domain.markup.template.node.Node
import com.meiblorn.kotidgy.domain.markup.template.node.processor.Filter
import com.meiblorn.kotidgy.domain.markup.template.node.processor.Map
import com.meiblorn.kotidgy.domain.markup.template.node.rule.AnyOf
import com.meiblorn.kotidgy.domain.markup.template.node.rule.Composition
import com.meiblorn.kotidgy.domain.markup.template.node.rule.Nothing
import com.meiblorn.kotidgy.domain.markup.template.node.rule.Optional
import com.meiblorn.kotidgy.dsl.collection.AllBag
import com.meiblorn.kotidgy.dsl.collection.AnyBag
import com.meiblorn.kotidgy.dsl.scope.Scope

@Suppress("MemberVisibilityCanBePrivate", "unused")
class TemplateScope : Scope() {

    fun c(supplier: () -> String): Const {
        return const(supplier)
    }

    fun const(supplier: () -> String): Const {
        return Const(supplier())
    }

    inline fun <reified T> o(supplier: () -> T): Optional {
        return optional(supplier)
    }

    inline fun <reified T> optional(supplier: () -> T): Optional {
        val value = supplier()
        return when (value) {
            is Node -> Optional(
                value
            )
            is String -> Optional(
                Const(
                    value
                )
            )
            else -> throw RuntimeException("Incompatible type argument is passed into optional node")
        }
    }

    inline fun <reified T> f(noinline supplier: () -> T): Lambda<T> {
        return lambda(supplier)
    }

    inline fun <reified T> call(noinline supplier: () -> T): Lambda<T> {
        return lambda(supplier)
    }

    inline fun <reified T> lambda(noinline supplier: () -> T): Lambda<T> {
        return Lambda(supplier)
    }

    fun map(consumer: (String) -> String): Map {
        return Map(consumer)
    }

    fun filter(consumer: (String) -> Boolean): Filter {
        return Filter(consumer)
    }

    operator fun String.unaryPlus(): Const {
        return Const(this)
    }

    operator fun <T : Node> T.unaryPlus(): T {
        return this
    }

    inline operator fun <reified T : Node> String.div(tag: T): AnyOf {
        return AnyOf(
            mutableListOf(
                Const(this),
                tag
            )
        )
    }

    inline operator fun <reified T : Node> Node.div(tag: T): AnyOf {
        return when (this) {
            is AnyOf -> {
                when (tag) {
                    is AnyOf -> {
                        this.alternatives.addAll(tag.alternatives)
                        this
                    }
                    else -> {
                        this.alternatives.add(tag)
                        this
                    }
                }
            }
            else -> AnyOf(mutableListOf(this, tag))
        }
    }

    inline operator fun <reified T : Node> T.div(value: String): AnyOf {
        val other = Const(value)
        return when (this) {
            is AnyOf -> {
                this.alternatives.add(other)
                return this
            }
            else -> AnyOf(mutableListOf(this, other))
        }
    }

    operator fun String.div(another: String): Node {
        return when {
            this.isEmpty() and another.isEmpty() -> Nothing()
            this.isNotEmpty() and another.isEmpty() -> Optional(Const(this))
            this.isEmpty() and another.isNotEmpty() -> Optional(Const(another))
            else -> AnyOf(
                mutableListOf(
                    Const(this),
                    Const(another)
                )
            )
        }
    }

    inline operator fun <reified T : Node> Node.plus(tag: T): Composition {
        return when (this) {
            is Composition -> when (tag) {
                is Composition -> {
                    this.nodes.addAll(tag.nodes)
                    this
                }
                else -> {
                    this.nodes.add(tag)
                    this
                }
            }
            else -> Composition(mutableListOf(this, tag))
        }
    }

    inline operator fun <reified T : Node> T.plus(value: String): Node {
        val other = Const(value)
        return when (this) {
            is Composition -> {
                this.nodes.add(other)
                this
            }
            else -> Composition(mutableListOf(this, other))
        }
    }

    inline operator fun <reified T : Node> T.rem(map: Map): T {
        this.processors.add(map)
        return this
    }

    operator fun String.rem(map: Map): Const {
        val tag = Const(this)
        tag.processors.add(map)
        return tag
    }

    fun <T> all(items: () -> Iterable<T>): AllBag<T> {
        return AllBag(items())
    }

    fun <T> any(items: () -> Iterable<T>): AnyBag<T> {
        return AnyBag(items())
    }

}