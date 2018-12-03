package com.meiblorn.kotidgy.domain.markup.template.node

class Lambda<T>(val supplier: () -> T) : Node()