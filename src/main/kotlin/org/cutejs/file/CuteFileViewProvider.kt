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
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteTypes
import org.cutejs.lang.CuteJSLanguage

class CuteFileViewProvider : MultiplePsiFilesPerDocumentFileViewProvider, TemplateLanguageFileViewProvider {
    private val myTemplateDataLanguage: Language

    constructor(manager: PsiManager, file: VirtualFile, physical: Boolean) : super(manager, file, physical) {
        var dataLang = TemplateDataLanguageMappings.getInstance(manager.project).getMapping(file)
        if (dataLang == null) dataLang = StdFileTypes.HTML.language

        myTemplateDataLanguage = if (dataLang is TemplateLanguage) {
            PlainTextLanguage.INSTANCE
        } else {
            LanguageSubstitutors.INSTANCE.substituteLanguage(dataLang, file, manager.project)
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
        return setOf(CuteLanguage.INSTANCE, CuteJSLanguage.INSTANCE, myTemplateDataLanguage)
    }

    override fun cloneInner(file: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
        return CuteFileViewProvider(manager, file, false, myTemplateDataLanguage)
    }

    override fun createFile(lang: Language): PsiFile? {
        val parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang) ?: return null

        return when (lang) {
            CuteLanguage.INSTANCE -> {
                return parserDefinition.createFile(this)
            }
            myTemplateDataLanguage -> {
                val file = parserDefinition.createFile(this) as PsiFileImpl
                file.contentElementType = TEMPLATE_MARKUP_DATA_TYPE

                return file
            }
            CuteJSLanguage.INSTANCE -> {
                val file = parserDefinition.createFile(this) as PsiFileImpl
                file.contentElementType = TEMPLATE_JS_DATA_TYPE

                return file
            }
            else -> null
        }
    }

    companion object {
        private val TEMPLATE_MARKUP_DATA_TYPE = TemplateDataElementType("TEMPLATE_MARKUP", CuteLanguage.INSTANCE,
                CuteTypes.T_DATA, CuteTypes.T_OUTER_DATA)
        private val TEMPLATE_JS_DATA_TYPE = CuteJSElementType("TEMPLATE_JS", CuteJSLanguage.INSTANCE,
                CuteTypes.T_EVAL_CHAR, CuteTypes.T_OUTER_DATA)
    }
}