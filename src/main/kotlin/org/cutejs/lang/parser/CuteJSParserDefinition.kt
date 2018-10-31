package org.cutejs.lang.parser

import com.intellij.lang.javascript.JavascriptParserDefinition
import com.intellij.psi.tree.IFileElementType
import org.cutejs.lang.CuteElementTypes

class CuteJSParserDefinition : JavascriptParserDefinition() {
    override fun getFileNodeType(): IFileElementType {
        return CuteElementTypes.FILE
    }
}