package org.cutejs.lang

import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSLanguageDialect

class CuteInnerJSLanguage protected constructor() : JSLanguageDialect("CuteInnerJS", DialectOptionHolder.OTHER) {
    override fun getFileExtension(): String {
        return "js"
    }

    companion object {
        val INSTANCE = CuteInnerJSLanguage()
    }
}