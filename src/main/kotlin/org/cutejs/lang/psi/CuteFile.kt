package org.cutejs.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.CuteFileType

import javax.swing.*

class CuteFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CuteLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return CuteFileType.INSTANCE
    }

    override fun toString(): String {
        return "CuteJSFile:${this.name}"
    }
}