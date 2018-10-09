package org.cutejs.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.cutejs.lang.psi.CuteElementFactory

open class CuteNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getNameIdentifier(): PsiElement? {
        return getNameIdentifier(this)
    }

    override fun setName(name: String): PsiElement {
        return setName(this, name)
    }

    companion object {
        fun getName(element: PsiElement): String {
            return element.text
        }

        fun setName(element: PsiElement, newName: String): PsiElement {
            val node = element.node
            if (node != null) {
                val identifier = CuteElementFactory.createNamespaceIdentifier(element.project, newName)
                val newIdentifierNode = identifier.node
                element.parent.node.replaceChild(node, newIdentifierNode)
            }
            return element
        }

        fun getNameIdentifier(element: PsiElement): PsiElement? {
            val lastIdentifier = element.node.lastChildNode
            return lastIdentifier.psi
        }
    }
}