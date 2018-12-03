package com.meiblorn.kotidgy.engine.renderer.node

import com.meiblorn.kotidgy.domain.markup.template.node.Node
import com.meiblorn.kotidgy.engine.renderer.Renderer

interface NodeRenderer<T : Node, out R> : Renderer<T, R>