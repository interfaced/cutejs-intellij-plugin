package org.cutejs.lang

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.templateLanguages.TemplateLanguage
import com.intellij.ide.highlighter.HtmlFileType

class CuteLanguage : Language("CuteTemplate", "text/x-cutejs-template"), TemplateLanguage {
    override fun getAssociatedFileType(): LanguageFileType {
        return CuteFileType.INSTANCE
    }

    companion object {
        val INSTANCE = CuteLanguage()
        fun getDefaultTemplateLang(): Language = HtmlFileType.INSTANCE.language
    }
}