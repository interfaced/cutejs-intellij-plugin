package org.cutejs.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.cutejs.lang.psi.CuteElementFactory

open class CuteNamespaceIdentifierExt(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getName(): String {
        return lastChild.text
    }

    override fun setName(newName: String): PsiElement {
        val identifier = CuteElementFactory.createNamespaceIdentifier(project, newName)
        val newIdentifierNode = identifier.node
        parent.node.replaceChild(node, newIdentifierNode)
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        val lastIdentifier = node.lastChildNode
        return lastIdentifier.psi
    }
}