package org.cutejs.lang.parser

import com.intellij.testFramework.ParsingTestCase
import org.cutejs.CuteTestUtil

class CuteParsingTestCase : ParsingTestCase("fixtures/parser", "jst", true, CuteParserDefinition()) {
    fun `test import`() = doTest(true)
    fun `test data`() = doTest(true)
    fun `test escape`() = doTest(true)
    fun `test interpolate`() = doTest(true)
    fun `test eval`() = doTest(true)
    fun `test export`() = doTest(true)
    fun `test typedef`() = doTest(true)
    fun `test inline`() = doTest(true)
    fun `test partial`() = doTest(true)
    fun `test namespace`() = doTest(true)

    override fun getTestDataPath(): String = "src/test/resources"
    override fun skipSpaces(): Boolean = false
    override fun includeRanges(): Boolean = true

    override fun getTestName(lowercaseFirstLetter: Boolean): String {
        val camelCase = super.getTestName(lowercaseFirstLetter)
        return CuteTestUtil.camelOrWordsToSnake(camelCase)
    }
}