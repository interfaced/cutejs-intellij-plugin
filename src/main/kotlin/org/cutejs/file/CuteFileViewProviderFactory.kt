package org.cutejs.file

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

class CuteFileViewProviderFactory : FileViewProviderFactory {
    override fun createFileViewProvider(file: VirtualFile, language: Language, manager: PsiManager, b: Boolean): FileViewProvider {
        return CuteFileViewProvider(manager, file, b)
    }
}