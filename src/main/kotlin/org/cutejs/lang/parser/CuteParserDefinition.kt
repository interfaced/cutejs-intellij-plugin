package org.cutejs.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.TokenType

import org.cutejs.lang.lexer.CuteLexer
import org.cutejs.lang.psi.CuteTypes.*
import org.cutejs.lang.psi.CuteFile
import org.cutejs.file.CuteFileElementType

class CuteParserDefinition : ParserDefinition {
    private val tsWHITESPACES = TokenSet.create(TokenType.WHITE_SPACE)

    override fun createParser(project: Project): PsiParser {
        return CuteParser()
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return CuteFile(viewProvider)
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getFileNodeType(): IFileElementType {
        return CuteFileElementType.INSTANCE
    }

    override fun getWhitespaceTokens(): TokenSet {
        return tsWHITESPACES
    }

    override fun createLexer(project: Project): Lexer {
        return CuteLexer()
    }

    override fun createElement(node: ASTNode): PsiElement {
        return Factory.createElement(node)
    }

    override fun getCommentTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    companion object {
        val OPEN_MARKERS = TokenSet.create(
                T_OPEN,
                T_ESCAPE,
                T_EXPORT,
                T_INTERPOLATE,
                T_PARTIAL,
                T_INLINE,
                T_NAMESPACE,
                T_TYPEDEF
        )
    }
}
