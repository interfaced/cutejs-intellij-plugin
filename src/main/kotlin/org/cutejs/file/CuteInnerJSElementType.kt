package org.cutejs.file

import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider

class CuteInnerJSElementType(debugName: String, language: Language, templateElementType: IElementType, outerElementType: IElementType) : TemplateDataElementType(debugName, language, templateElementType, outerElementType) {
    override fun getTemplateFileLanguage(viewProvider: TemplateLanguageFileViewProvider): Language {
        return language
    }
}