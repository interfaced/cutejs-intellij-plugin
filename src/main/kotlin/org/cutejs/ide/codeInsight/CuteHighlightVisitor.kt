package org.cutejs.ide.codeInsight

import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import org.cutejs.lang.psi.CuteExportArgs
import org.cutejs.lang.psi.CuteFile
import org.cutejs.lang.psi.CuteNamespace
import org.cutejs.lang.psi.CuteTypedef
import org.cutejs.lang.psi.visitor.CuteRecursiveElementVisitor
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import org.cutejs.ide.highlight.CuteHighlighter


class CuteHighlightVisitor : CuteRecursiveElementVisitor(), HighlightVisitor {
    private var infoHolder: HighlightInfoHolder? = null

    override fun visitNamespace(element: CuteNamespace) {
        val lastNamespaceIdentifier = element.namespaceArgs.namespaceIdentifier?.lastChild

        if (lastNamespaceIdentifier != null) {
            val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
            builder.textAttributes(CuteHighlighter.DECLARATION)
            builder.range(lastNamespaceIdentifier)

            infoHolder?.add(builder.create())
        }
    }

    override fun visitExportArgs(element: CuteExportArgs) {
        val identifier = element.firstChild

        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(CuteHighlighter.PROPERTY)
        builder.range(identifier)

        infoHolder?.add(builder.create())
    }

    override fun visitTypedef(element: CuteTypedef) {
        val property = element.thisProperty.lastChild

        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(CuteHighlighter.PROPERTY)
        builder.range(property)

        infoHolder?.add(builder.create())
    }

    override fun analyze(file: PsiFile, updateWholeFile: Boolean, holder: HighlightInfoHolder, action: Runnable): Boolean {
        infoHolder = holder
        action.run()

        return true
    }

    override fun visit(element: PsiElement) {
        element.accept(this)
    }

    override fun visitWhiteSpace(space: PsiWhiteSpace?) {}

    override fun clone(): HighlightVisitor = CuteHighlightVisitor()

    override fun suitableForFile(file: PsiFile): Boolean = file is CuteFile

    override fun order(): Int = 1
}