package org.cutejs.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.cutejs.lang.psi.CuteTypes.*
import org.cutejs.lang.lexer.CuteLexer

class CuteHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return CuteLexer()
    }

    override fun getTokenHighlights(type: IElementType): Array<TextAttributesKey> {
        return when (type) {
            T_OPEN, T_CLOSE -> OPEN_CLOSE_KEYS
            T_NAMESPACE,
            T_EXPORT,
            T_TYPEDEF,
            T_ESCAPE,
            T_INTERPOLATE,
            T_INLINE,
            T_PARTIAL -> KEYWORD_KEYS
            T_IDENTIFIER -> IDENTIFIER_KEYS
            T_COMMA -> COMMA_KEYS
            T_ARRAY_SPECIFIER -> BRACKET_KEYS
            T_DOC_TYPE -> DOC_KEYS
            else -> EMPTY
        }
    }

    companion object {
        private val OPEN_CLOSE = TextAttributesKey.createTextAttributesKey("OPEN_CLOSE_TEMPLATE", METADATA)
        private val KEYWORD_ = TextAttributesKey.createTextAttributesKey("KEYWORD", KEYWORD)
        private val DOC = TextAttributesKey.createTextAttributesKey("TYPE", DOC_COMMENT)
        private val COMMA_ = TextAttributesKey.createTextAttributesKey("COMMA", COMMA)
        private val PAIRED_BRACKETS = TextAttributesKey.createTextAttributesKey("PAIRED_BRACKETS", BRACKETS)
        private val IDENTIFIER_ = TextAttributesKey.createTextAttributesKey("ID", IDENTIFIER)

        val OPEN_CLOSE_KEYS = arrayOf(OPEN_CLOSE)
        val KEYWORD_KEYS = arrayOf(KEYWORD_)
        val DOC_KEYS = arrayOf(DOC)
        val COMMA_KEYS = arrayOf(COMMA_)
        val BRACKET_KEYS = arrayOf(PAIRED_BRACKETS)
        val IDENTIFIER_KEYS = arrayOf(IDENTIFIER_)

        private val EMPTY = arrayOf<TextAttributesKey>()
    }
}