package org.cutejs.ide.typing

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

import org.cutejs.lang.psi.CuteTypes.*

class CuteBraceMatcher : PairedBraceMatcher {
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    override fun getPairs(): Array<BracePair> {
        return OPEN_BRACE.types.map {
            BracePair(it, T_CLOSE_BLOCK_MARKER, true)
        }.toTypedArray()
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    companion object {
        fun tokenSetOf(vararg tokens: IElementType) = TokenSet.create(*tokens)
        private val OPEN_BRACE = tokenSetOf(
                T_OPEN_BLOCK_MARKER,
                T_OPEN_BLOCK_MARKER_ESCAPED,
                T_OPEN_BLOCK_MARKER_EXPORT_DATA,
                T_OPEN_BLOCK_MARKER_UNESCAPED,
                T_OPEN_BLOCK_MARKER_INCLUDE,
                T_OPEN_BLOCK_MARKER_INLINE,
                T_OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION,
                T_OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION
        )
    }
}