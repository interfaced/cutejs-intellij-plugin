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

class CuteInnerJSElementType(debugName: String, language: Language, templateElementType: IElementType, outerElementType: IElementType) : TemplateDataElementType(debugName, language, templateElementType, outerElementType) {
    private val offsets = ThreadLocal<Stack<LinkedList<Int>>>()

    override fun getTemplateFileLanguage(viewProvider: TemplateLanguageFileViewProvider): Language {
        return language
    }

    override fun createTemplateText(sourceCode: CharSequence, baseLexer: Lexer, outerRangesCollector: RangeCollector): CharSequence {
        if (offsets.get() == null) {
            offsets.set(Stack())
        }

        offsets.get().push(LinkedList())

        return super.createTemplateText(sourceCode, baseLexer, outerRangesCollector)
    }

    override fun appendCurrentTemplateToken(result: StringBuilder, buf: CharSequence, lexer: Lexer, collector: RangeCollector) {
        super.appendCurrentTemplateToken(result, buf, lexer, collector)

        offsets.get().peek().add(result.length)
        result.append('\n')
    }

    override fun prepareParsedTemplateFile(root: FileElement) {
        root.acceptTree(LineBreaksFixingElementVisitor(offsets.get().pop()))
        root.acceptTree(LabeledStatementElementVisitor())
    }
}