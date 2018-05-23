package org.cutejs.lang.parser

import com.intellij.lang.javascript.JavascriptParserDefinition
import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.psi.tree.IFileElementType

import org.cutejs.lang.CuteInnerJSLanguage

class CuteInnerJSParserDefinition : JavascriptParserDefinition() {
    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    companion object {
        private val FILE = JSFileElementType.create(CuteInnerJSLanguage.INSTANCE)
    }
}