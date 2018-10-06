package org.cutejs.lang

import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSLanguageDialect

class CuteJSLanguage private constructor() : JSLanguageDialect("CuteJS", DialectOptionHolder.ECMA_6) {
    override fun getFileExtension(): String {
        return "js"
    }

    companion object {
        val INSTANCE = CuteJSLanguage()
    }
}