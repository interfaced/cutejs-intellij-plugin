package org.cutejs.lang

import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.psi.tree.IFileElementType

object CuteElementTypes {
    val FILE: IFileElementType = JSFileElementType.create(CuteJSLanguage.INSTANCE)
}