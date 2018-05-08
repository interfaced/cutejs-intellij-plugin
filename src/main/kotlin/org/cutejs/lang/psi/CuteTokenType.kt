package org.cutejs.lang.psi

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.*

import org.cutejs.lang.CuteLanguage

class CuteTokenType(@NonNls debugName: String) : IElementType(debugName, CuteLanguage.INSTANCE) {
    override fun toString(): String = "CuteTokenType.${super.toString()}"
}