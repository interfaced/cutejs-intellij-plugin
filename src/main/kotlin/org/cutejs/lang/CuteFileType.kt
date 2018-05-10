package org.cutejs.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.*

import org.cutejs.ide.icons.CuteIcons

open class CuteFileType : LanguageFileType(CuteLanguage.INSTANCE) {
    override fun getName(): String {
        return "CuteJSTemplate"
    }

    override fun getDescription(): String {
        return "CuteJS Template language file"
    }

    override fun getDefaultExtension(): String {
        return "jst"
    }

    override fun getIcon(): Icon? {
        return CuteIcons.ICON
    }

    companion object {
        val INSTANCE = CuteFileType()
    }
}