package org.cutejs.file

import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.lang.Language
import com.intellij.lexer.Lexer
import com.intellij.openapi.util.TextRange
import com.intellij.psi.tree.IElementType
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider

class CuteJSElementType(
        debugName: String,
        language: Language,
        private val templateElementType: IElementType,
        private val expressionElementType: IElementType,
        outerElementType: IElementType
) : TemplateDataElementType(debugName, language, templateElementType, outerElementType) {
    override fun getTemplateFileLanguage(viewProvider: TemplateLanguageFileViewProvider): Language {
        return language
    }

    override fun createTemplateText(sourceCode: CharSequence, baseLexer: Lexer, rangeCollector: RangeCollector): CharSequence {
        val result = StringBuilder(sourceCode.length)
        baseLexer.start(sourceCode)

        var currentRange = TextRange.EMPTY_RANGE
        while (baseLexer.tokenType != null) {
            val newRange = TextRange.create(baseLexer.tokenStart, baseLexer.tokenEnd)

            assert(currentRange.endOffset == newRange.startOffset) {
                "Inconsistent tokens stream from $baseLexer: $currentRange followed by $newRange"
            }

            currentRange = newRange
            when (baseLexer.tokenType) {
                templateElementType -> appendCurrentTemplateToken(result, sourceCode, baseLexer, rangeCollector)
                expressionElementType -> appendCurrentExpressionToken(result, baseLexer, rangeCollector)
                else -> rangeCollector.addOuterRange(newRange)
            }
            baseLexer.advance()
        }

        return result
    }

    private fun appendCurrentExpressionToken(result: java.lang.StringBuilder, lexer: Lexer, collector: RangeCollector) {
        val wrappedExpr = "(${lexer.tokenText});"
        collector.addRangeToRemove(TextRange(lexer.tokenStart, lexer.tokenStart + 1))
        collector.addRangeToRemove(TextRange(lexer.tokenEnd, lexer.tokenEnd + 2))

        result.append(wrappedExpr)
    }
}