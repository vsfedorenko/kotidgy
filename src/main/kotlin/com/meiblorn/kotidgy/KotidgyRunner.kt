package com.meiblorn.kotidgy

import com.meiblorn.kotidgy.domain.markup.Project
import com.meiblorn.kotidgy.engine.KotidgyEngine
import com.meiblorn.kotidgy.util.ScriptLoader
import java.nio.file.Files
import java.nio.file.Paths

class KotidgyRunner {
    companion object {
        val engine = KotidgyEngine()

        @JvmStatic
        fun main(args: Array<String>) {
            val scriptReader = Files.newBufferedReader(Paths.get(args[0]))
            val project: Project = ScriptLoader.load(scriptReader)
            for (sample in engine.generate(project)) {
                println(sample)
            }
        }
    }
}