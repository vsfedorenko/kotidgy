package com.meiblorn.kotidgy.engine

import com.meiblorn.kotidgy.domain.generation.sample.Sample
import com.meiblorn.kotidgy.domain.generation.sample.id.IndexId
import com.meiblorn.kotidgy.domain.markup.Project
import com.meiblorn.kotidgy.dsl.project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class KotidgyEngineTest {

    private lateinit var engine: KotidgyEngine

    @BeforeEach
    fun setUp() {
        engine = KotidgyEngine()
    }

    @AfterEach
    fun tearDown() {

    }

    @ParameterizedTest()
    @MethodSource("projectSource")
    fun `Test project generation`(project: Project, expectedSamples: List<Sample<*>>) {
        val actualSamples = engine.generate(project).toList()
        assertEquals(expectedSamples.size, actualSamples.size)
        assertIterableEquals(expectedSamples, actualSamples)
    }

    companion object {
        @JvmStatic
        fun projectSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    project {
                        templates {
                            t { +"Hello" }
                            t { +"Hi" / "Aloha" }
                            t { +f { 2..3 } + " apples" }
                        }
                    },
                    listOf(
                        Sample(IndexId(0), "Hello"),
                        Sample(IndexId(0), "Hi"),
                        Sample(IndexId(1), "Aloha"),
                        Sample(
                            IndexId(0),
                            "2",
                            Sample(IndexId(0), " apples")
                        ),
                        Sample(
                            IndexId(1),
                            "3",
                            Sample(IndexId(0), " apples")
                        )
                    )
                )
            )
        }
    }
}