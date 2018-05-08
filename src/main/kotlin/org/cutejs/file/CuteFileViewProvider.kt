package org.cutejs.file

import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.psi.templateLanguages.TemplateLanguage
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import com.intellij.psi.tree.IElementType
import gnu.trove.THashSet
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteTypes

import java.util.Arrays

class CuteFileViewProvider : MultiplePsiFilesPerDocumentFileViewProvider, TemplateLanguageFileViewProvider {
    private val myTemplateDataLanguage: Language

    constructor(manager: PsiManager, file: VirtualFile, physical: Boolean) : super(manager, file, physical) {

        var dataLang = TemplateDataLanguageMappings.getInstance(manager.project).getMapping(file)
        if (dataLang == null) dataLang = StdFileTypes.HTML.language

        if (dataLang is TemplateLanguage) {
            myTemplateDataLanguage = PlainTextLanguage.INSTANCE
        } else {
            myTemplateDataLanguage = LanguageSubstitutors.INSTANCE.substituteLanguage(dataLang, file, manager.project)
        }
    }

    constructor(psiManager: PsiManager, virtualFile: VirtualFile, physical: Boolean, myTemplateDataLanguage: Language) : super(psiManager, virtualFile, physical) {
        this.myTemplateDataLanguage = myTemplateDataLanguage
    }


    override fun getBaseLanguage(): Language {
        return CuteLanguage.INSTANCE
    }

    override fun getTemplateDataLanguage(): Language {
        return myTemplateDataLanguage
    }

    override fun getLanguages(): Set<Language> {
        return THashSet(Arrays.asList(*arrayOf(CuteLanguage.INSTANCE, myTemplateDataLanguage)))
    }


    override fun cloneInner(file: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
        return CuteFileViewProvider(manager, file, false, myTemplateDataLanguage)
    }

    override fun createFile(lang: Language): PsiFile? {
        if (lang === myTemplateDataLanguage) {
            val file = LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this) as PsiFileImpl
            file.contentElementType = TEMPLATE_DATA
            return file
        } else return if (lang === CuteLanguage.INSTANCE) {
            LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this)
        } else {
            null
        }
    }

    companion object {
        private val CUTE_FRAGMENT = IElementType("CuteFragmentElementType", CuteLanguage.INSTANCE)
        val TEMPLATE_DATA: IElementType = TemplateDataElementType("CuteTextElementType", CuteLanguage.INSTANCE, CuteTypes.T_TEMPLATE_HTML_CODE, CUTE_FRAGMENT)
    }
}