package org.cutejs.ide

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
        return arrayOf(BracePair(T_OPEN, T_CLOSE, true))
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}