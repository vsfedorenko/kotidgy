package com.meiblorn.kotidgy.util

import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

object ScriptLoader {

    private val classLoader: ClassLoader = Thread.currentThread().contextClassLoader

    val scriptEngine: ScriptEngine = ScriptEngineManager(classLoader).getEngineByExtension("kts")

    inline fun <reified T> load(reader: Reader): T = scriptEngine.eval(reader).takeIf { it is T } as T
        ?: throw IllegalStateException("Could not load script from .kts")

    inline fun <reified T> load(inputStream: InputStream): T = load<T>(inputStream.reader())
        ?: throw IllegalStateException("Could not load script from .kts")

}