package org.cutejs.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.cutejs.lang.psi.CuteTypes
import org.cutejs.lang.lexer.CuteLexer

class CuteHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return CuteLexer()
    }

    override fun getTokenHighlights(type: IElementType): Array<TextAttributesKey> {
        return when (type) {
            CuteTypes.T_OPEN,
            CuteTypes.T_CLOSE,
            CuteTypes.T_NAMESPACE,
            CuteTypes.T_EXPORT,
            CuteTypes.T_TYPEDEF,
            CuteTypes.T_ESCAPE,
            CuteTypes.T_INTERPOLATE,
            CuteTypes.T_INLINE,
            CuteTypes.T_PARTIAL -> OPENCLOSETAG_KEYS
            CuteTypes.T_IDENTIFIER -> EXPORT_ID_KEYS
            CuteTypes.T_COMMA -> COMMA_KEYS
            CuteTypes.T_ARRAY_SPECIFIER -> BRACKET_KEYS
            else -> EMPTY
        }
    }

    companion object {
        private val OPENCLOSETAG = TextAttributesKey.createTextAttributesKey("OPEN_CLOSE_TEMPLATE",
                DefaultLanguageHighlighterColors.METADATA)
        private val EXPORT_ID = TextAttributesKey.createTextAttributesKey("EXPORT_ID", DefaultLanguageHighlighterColors.KEYWORD)
        private val TYPE_ID = TextAttributesKey.createTextAttributesKey("TYPE_ID", DefaultLanguageHighlighterColors.DOC_COMMENT)
        private val COMMA = TextAttributesKey.createTextAttributesKey("COMMA_SEPARATOR", DefaultLanguageHighlighterColors.COMMA)
        private val PAIRED_BRACKETS = TextAttributesKey.createTextAttributesKey("PAIRED_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)

        val OPENCLOSETAG_KEYS = arrayOf(OPENCLOSETAG)
        val EXPORT_ID_KEYS = arrayOf(EXPORT_ID)
        val TYPE_ID_KEYS = arrayOf(TYPE_ID)
        val COMMA_KEYS = arrayOf(COMMA)
        val BRACKET_KEYS = arrayOf(PAIRED_BRACKETS)

        private val EMPTY = arrayOf<TextAttributesKey>()
    }
}