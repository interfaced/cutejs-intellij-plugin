package org.cutejs.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet

class CuteLexer : MergingLexerAdapter(FlexAdapter(_CuteLexer()), TokenSet.EMPTY)