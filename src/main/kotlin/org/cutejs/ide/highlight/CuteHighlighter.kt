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
            else -> EMPTY
        }
    }

    companion object {
        private val OPENCLOSETAG = TextAttributesKey.createTextAttributesKey("OPEN_CLOSE_TEMPLATE",
                DefaultLanguageHighlighterColors.METADATA)

        val OPENCLOSETAG_KEYS = arrayOf(OPENCLOSETAG)

        private val EMPTY = arrayOf<TextAttributesKey>()
    }
}