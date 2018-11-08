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
            T_THIS,
            T_IMPORT,
            T_PARTIAL -> KEYWORD_KEYS
            T_IDENTIFIER -> IDENTIFIER_KEYS
            T_COMMA -> COMMA_KEYS
            T_ARRAY_SPECIFIER -> BRACKET_KEYS
            T_DOC_TYPE -> DOC_KEYS
            else -> EMPTY
        }
    }

    companion object {
        val OPEN_CLOSE = TextAttributesKey.createTextAttributesKey("OPEN_CLOSE_TEMPLATE", METADATA)
        val KEYWORD_ = TextAttributesKey.createTextAttributesKey("KEYWORD", KEYWORD)
        val DOC = TextAttributesKey.createTextAttributesKey("TYPE", DOC_COMMENT)
        val COMMA_ = TextAttributesKey.createTextAttributesKey("COMMA", COMMA)
        val PAIRED_BRACKETS = TextAttributesKey.createTextAttributesKey("PAIRED_BRACKETS", BRACKETS)
        val IDENTIFIER_ = TextAttributesKey.createTextAttributesKey("ID", IDENTIFIER)
        val DECLARATION = TextAttributesKey.createTextAttributesKey("DECLARATION", FUNCTION_DECLARATION)
        val PROPERTY = TextAttributesKey.createTextAttributesKey("PROP", INSTANCE_FIELD)

        private val OPEN_CLOSE_KEYS = arrayOf(OPEN_CLOSE)
        private val KEYWORD_KEYS = arrayOf(KEYWORD_)
        private val DOC_KEYS = arrayOf(DOC)
        private val COMMA_KEYS = arrayOf(COMMA_)
        private val BRACKET_KEYS = arrayOf(PAIRED_BRACKETS)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER_)

        private val EMPTY = arrayOf<TextAttributesKey>()
    }
}