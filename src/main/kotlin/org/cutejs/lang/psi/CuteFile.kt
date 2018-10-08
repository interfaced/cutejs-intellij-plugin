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
    fun templateNamespace(): String? {
        val visitor = NamespaceVisitor()
        this.acceptChildren(visitor)

        return visitor.getNamespaceText()
    }

    override fun getFileType(): FileType {
        return CuteFileType.INSTANCE
    }

    override fun toString(): String {
        return "CuteJSFile:${this.name}"
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

    fun getNamespaceText() : String? {
        val namespaceIdentifier = namespace?.namespaceArgs?.namespaceIdentifier

        return namespaceIdentifier?.text
    }
}