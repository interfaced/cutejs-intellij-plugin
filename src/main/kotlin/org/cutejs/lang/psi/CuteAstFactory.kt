package org.cutejs.lang.psi

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl
import com.intellij.psi.tree.IElementType

class CuteAstFactory : DefaultASTFactoryImpl() {
    override fun createLeaf(type: IElementType, text: CharSequence): LeafElement {
        if (type is CuteTokenType) {
            return OuterLanguageElementImpl(type, text)
        }
        return super.createLeaf(type, text)
    }
}