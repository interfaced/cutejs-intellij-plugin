package org.cutejs.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.CuteFileType
import org.cutejs.lang.psi.impl.CuteStatementImpl

class CuteFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CuteLanguage.INSTANCE) {
    private var generatedFile: CuteGeneratedFile? = null

    fun getOrFindGeneratedFile(): CuteGeneratedFile? {
        if (generatedFile == null || generatedFile?.file?.virtualFile?.exists() == false) {
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

    fun templateNamespace(): CuteNamespace? {
        val statement = PsiTreeUtil.getChildOfType(this, CuteStatementImpl::class.java)
        return statement?.expression?.namespace
    }

    private fun findGeneratedFile(): CuteGeneratedFile? {
        val templateNamespace = templateNamespace()?.ref?.text ?: return null
        return CuteResolveUtil.findGeneratedFileByNamespace(templateNamespace, project)
    }
}