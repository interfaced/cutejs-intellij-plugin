package org.cutejs.file

import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.lang.Language
import com.intellij.lexer.Lexer
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import org.cutejs.lang.psi.visitor.LabeledStatementElementVisitor
import org.cutejs.lang.psi.visitor.LineBreaksFixingElementVisitor
import java.lang.StringBuilder
import java.util.*
import java.util.LinkedList

class CuteJSElementType(debugName: String, language: Language, templateElementType: IElementType, outerElementType: IElementType) : TemplateDataElementType(debugName, language, templateElementType, outerElementType) {
    override fun getTemplateFileLanguage(viewProvider: TemplateLanguageFileViewProvider): Language {
        return language
    }
}