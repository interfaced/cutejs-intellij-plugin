package org.cutejs.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
import org.cutejs.lang.psi.CuteTypes

class CuteLexer : MergingLexerAdapter(FlexAdapter(_CuteLexer()), TokenSet.create(CuteTypes.COMMENT))