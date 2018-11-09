package org.cutejs.lang.parser

import com.intellij.lang.PsiParser
import com.intellij.lang.javascript.JSFlexAdapter
import com.intellij.lang.javascript.JavascriptParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IFileElementType
import org.cutejs.lang.CuteElementTypes
import org.cutejs.lang.CuteJSLanguage

class CuteJSParserDefinition : JavascriptParserDefinition() {
    override fun getFileNodeType(): IFileElementType {
        return CuteElementTypes.FILE
    }

    override fun createParser(project: Project?): PsiParser {
        return PsiParser { root, builder ->
            CuteJSLanguage.INSTANCE.createParser(builder).parseJS(root)
            return@PsiParser builder.treeBuilt
        }
    }

    override fun createLexer(project: Project?): Lexer {
        return JSFlexAdapter(CuteJSLanguage.INSTANCE.optionHolder)
    }
}