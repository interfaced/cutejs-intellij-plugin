package org.cutejs.lang.psi.visitor

import com.intellij.psi.PsiElement

open class CuteRecursiveElementVisitor : CuteElementVisitor() {
    override fun visitElement(element: PsiElement?) {
        super.visitElement(element)
        element?.acceptChildren(this)
    }
}