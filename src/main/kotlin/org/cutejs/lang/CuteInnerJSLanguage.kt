package org.cutejs.lang

import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSLanguageDialect

class CuteInnerJSLanguage private constructor() : JSLanguageDialect("CuteInnerJS", DialectOptionHolder.ECMA_6) {
    override fun getFileExtension(): String {
        return "js"
    }

    companion object {
        val INSTANCE = CuteInnerJSLanguage()
    }
}