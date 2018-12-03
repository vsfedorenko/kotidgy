package com.meiblorn.kotidgy.domain.generation.sample

import com.meiblorn.kotidgy.domain.generation.sample.id.Id

open class Sample<ID : Id>(val id: ID, val content: String, var next: Sample<*>? = null) {

    operator fun plus(forwardItem: Sample<*>): Sample<ID> {
        this.next = forwardItem
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sample<*>

        if (id != other.id) return false
        if (content != other.content) return false
        if (next != other.next) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (next?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return content + (next?.toString() ?: "")
    }
}