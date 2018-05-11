package org.cutejs.lang.parser

import org.cutejs.lang.CuteInnerJSLanguage

import com.intellij.lang.javascript.nashorn.NashornJSParserDefinition;
import com.intellij.lang.javascript.types.JSFileElementType;
import com.intellij.psi.tree.IFileElementType;

class CuteInnerJSParserDefinition : NashornJSParserDefinition() {
    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    companion object {
        private val FILE = JSFileElementType.create(CuteInnerJSLanguage.INSTANCE)
    }
}