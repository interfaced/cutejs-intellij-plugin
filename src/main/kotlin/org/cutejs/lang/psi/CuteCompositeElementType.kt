package org.cutejs.lang.psi

import com.intellij.psi.tree.IElementType
import org.cutejs.lang.CuteLanguage

class CuteCompositeElementType(expr: String) : IElementType(expr, CuteLanguage.INSTANCE)