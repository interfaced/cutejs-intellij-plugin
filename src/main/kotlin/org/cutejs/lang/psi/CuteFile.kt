package org.cutejs.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.getChildOfType

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.CuteFileType
import org.cutejs.lang.psi.impl.CuteExpressionImpl
import org.cutejs.lang.psi.impl.CuteNamespaceImpl

class CuteFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CuteLanguage.INSTANCE) {
    private var generatedFile: PsiFile? = null

    fun getOrFindGeneratedFile(): PsiFile? {
        if (generatedFile == null || generatedFile?.virtualFile?.exists() == false) {
            generatedFile = findGeneratedFile()
        }

        return generatedFile
    }

    override fun getFileType(): FileType {
        return CuteFileType.INSTANCE
    }

    override fun toString(): String {
        return "CuteJSFile:${this.name}"
    }

    private fun findGeneratedFile(): PsiFile? {
        val templateNamespace = templateNamespaceIdentifierText() ?: return null
        return CuteResolveUtil.findGeneratedFileByNamespace(templateNamespace, project)
    }

    private fun templateNamespaceIdentifierText(): String? {
        val visitor = NamespaceVisitor()
        this.acceptChildren(visitor)

        return visitor.getNamespaceIdentifierText()
    }
}

class NamespaceVisitor : PsiElementVisitor() {
    private var namespace: CuteNamespace? = null

    override fun visitElement(element: PsiElement?) {
        if (namespace == null) {
            val expression = getChildOfType(element, CuteExpressionImpl::class.java)
            namespace = getChildOfType(expression, CuteNamespaceImpl::class.java)
        }

        super.visitElement(element)
    }

    fun getNamespaceIdentifierText() : String? {
        val namespaceIdentifier = namespace?.namespaceArgs?.namespaceIdentifier

        return namespaceIdentifier?.text
    }
}