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
            CuteTypes.T_OPEN_BLOCK_MARKER,
            CuteTypes.T_CLOSE_BLOCK_MARKER,
            CuteTypes.T_OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION,
            CuteTypes.T_OPEN_BLOCK_MARKER_EXPORT_DATA,
            CuteTypes.T_OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION,
            CuteTypes.T_OPEN_BLOCK_MARKER_ESCAPED,
            CuteTypes.T_OPEN_BLOCK_MARKER_UNESCAPED,
            CuteTypes.T_OPEN_BLOCK_MARKER_INLINE,
            CuteTypes.T_OPEN_BLOCK_MARKER_INCLUDE -> OPENCLOSETAG_KEYS
            CuteTypes.T_EXPORT_IDENTIFIER -> EXPORT_ID_KEYS
            CuteTypes.T_TYPE_IDENTIFIER -> TYPE_ID_KEYS
            CuteTypes.T_COMMA_SEPARATOR -> COMMA_KEYS
            CuteTypes.T_ARRAY_MODIFIER -> BRACKET_KEYS
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