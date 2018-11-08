package org.cutejs.file

import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteTypes
import org.cutejs.lang.CuteJSLanguage

class CuteFileViewProvider(psiManager: PsiManager, virtualFile: VirtualFile, physical: Boolean)
    : MultiplePsiFilesPerDocumentFileViewProvider(psiManager, virtualFile, physical), TemplateLanguageFileViewProvider {
    private val templateDataLanguage: Language = CuteLanguage.getDefaultTemplateLang()

    override fun getBaseLanguage(): Language {
        return CuteLanguage.INSTANCE
    }

    override fun getTemplateDataLanguage(): Language {
        return templateDataLanguage
    }

    override fun getLanguages(): Set<Language> {
        return setOf(CuteLanguage.INSTANCE, CuteJSLanguage.INSTANCE, templateDataLanguage)
    }

    override fun cloneInner(file: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
        return CuteFileViewProvider(manager, file, false)
    }

    override fun createFile(lang: Language): PsiFile? {
        val parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang) ?: return null

        return when (lang) {
            CuteLanguage.INSTANCE -> {
                return parserDefinition.createFile(this)
            }
            templateDataLanguage -> {
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
                CuteTypes.T_EVAL, CuteTypes.T_EVAL_EXPRESSION, CuteTypes.T_OUTER_DATA)
    }
}