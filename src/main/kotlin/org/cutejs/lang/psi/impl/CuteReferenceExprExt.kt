package org.cutejs.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.cutejs.lang.psi.CuteElementFactory

open class CuteReferenceExprExt(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getName(): String = firstChild.lastChild.text

    override fun setName(newName: String): PsiElement {
        val ref = CuteElementFactory.createNamespace(project, newName).ref
        val newRefNode = ref.node
        parent.node.replaceChild(node, newRefNode)
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        val lastIdentifier = firstChild.node.lastChildNode
        return lastIdentifier.psi
    }
}