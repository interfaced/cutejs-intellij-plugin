package org.cutejs.ide.highlight

import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import org.cutejs.lang.psi.*

class CuteHighlightVisitor : CuteVisitor(), HighlightVisitor {
    private var infoHolder: HighlightInfoHolder? = null

    override fun visitNamespace(element: CuteNamespace) {
        val ref = element.ref
        val cuteFile = element.containingFile as CuteFile
        val generatedFile = cuteFile.getOrFindGeneratedFile()

        if (generatedFile != null && generatedFile.namespace == ref.node.text) {
            val lastIdentifier = ref.expr.lastChild
            highlightDeclaration(lastIdentifier)
        }
    }

    override fun visitExportArgs(element: CuteExportArgs) {
        highlightProperty(element.firstChild)
    }

    override fun visitIncludeArgs(o: CuteIncludeArgs) {
        val ref = o.ref

        if (CuteResolveUtil.resolveReference(ref) != null) {
            val lastIdentifier = ref.expr.lastChild
            highlightDeclaration(lastIdentifier)
        }
    }

    override fun visitTypedef(element: CuteTypedef) {
        highlightProperty(element.thisProperty.lastChild)
    }

    override fun visit(element: PsiElement) {
        element.accept(this)
    }

    override fun analyze(file: PsiFile, updateWholeFile: Boolean, holder: HighlightInfoHolder, action: Runnable): Boolean {
        infoHolder = holder
        action.run()

        return true
    }

    override fun visitWhiteSpace(space: PsiWhiteSpace?) {}

    override fun clone(): HighlightVisitor = CuteHighlightVisitor()

    override fun suitableForFile(file: PsiFile): Boolean = file is CuteFile

    override fun order(): Int = 1

    private fun highlightProperty(element: PsiElement) {
        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(CuteHighlighter.PROPERTY)
        builder.range(element)

        infoHolder?.add(builder.create())
    }

    private fun highlightDeclaration(element: PsiElement) {
        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(CuteHighlighter.DECLARATION)
        builder.range(element)

        infoHolder?.add(builder.create())
    }
}