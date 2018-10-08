package org.cutejs.lang.psi.visitor

import com.intellij.psi.PsiElement
import org.cutejs.lang.psi.CuteExportArgs
import org.cutejs.lang.psi.CuteNamespace
import org.cutejs.lang.psi.CuteTypedef

open class CuteRecursiveElementVisitor : CuteElementVisitor() {
    override fun visitElement(element: PsiElement?) {
        when (element) {
            is CuteNamespace -> visitNamespace(element)
            is CuteExportArgs -> visitExportArgs(element)
            is CuteTypedef -> visitTypedef(element)
        }

        element?.acceptChildren(this)
    }
}