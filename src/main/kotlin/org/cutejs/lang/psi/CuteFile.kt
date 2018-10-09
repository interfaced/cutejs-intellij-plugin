package org.cutejs.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.CuteFileType
import org.cutejs.lang.psi.impl.CuteStatementImpl

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

    fun templateNamespaceIdentifier(): CuteNamespaceIdentifier? {
        val statement = PsiTreeUtil.getChildOfType(this, CuteStatementImpl::class.java)
        val namespace = statement?.expression?.namespace

        return namespace?.namespaceArgs?.namespaceIdentifier
    }

    private fun findGeneratedFile(): PsiFile? {
        val templateNamespace = templateNamespaceIdentifier()?.text ?: return null
        return CuteResolveUtil.findGeneratedFileByNamespace(templateNamespace, project)
    }
}