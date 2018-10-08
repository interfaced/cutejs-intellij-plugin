package org.cutejs.lang.psi.visitor

import com.intellij.psi.PsiElementVisitor
import org.cutejs.lang.psi.CuteExportArgs
import org.cutejs.lang.psi.CuteNamespace
import org.cutejs.lang.psi.CuteTypedef

open class CuteElementVisitor : PsiElementVisitor() {
    open fun visitNamespace(element: CuteNamespace) {}
    open fun visitExportArgs(element: CuteExportArgs) {}
    open fun visitTypedef(element: CuteTypedef) {}
}