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
import org.cutejs.lang.psi.CuteTypes
import org.cutejs.lang.psi.CuteFile
import org.cutejs.file.CuteFileElementType

class CuteParserDefinition : ParserDefinition {
    private val tsWHITESPACES = TokenSet.create(TokenType.WHITE_SPACE)
    private val tsCOMMENTS = TokenSet.create(CuteTypes.COMMENT)

    override fun createParser(project: Project): PsiParser {
        return CuteParser()
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return CuteFile(viewProvider)
    }

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
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
        return CuteTypes.Factory.createElement(node)
    }

    override fun getCommentTokens(): TokenSet {
        return tsCOMMENTS
    }
}
