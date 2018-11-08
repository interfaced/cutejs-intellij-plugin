package org.cutejs.ide.highlight

import com.intellij.lang.javascript.dialects.ECMA6SyntaxHighlighterFactory
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.cutejs.lang.CuteJSLanguage
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteTypes.*

class CuteLayeredHighlighter(project: Project?, virtualFile: VirtualFile?, colors: EditorColorsScheme) : LayeredLexerEditorHighlighter(CuteHighlighter(), colors) {
    init {
        val jsHighlighter = ECMA6SyntaxHighlighterFactory.ECMA6SyntaxHighlighter(CuteJSLanguage.INSTANCE.optionHolder, false)
        registerLayer(T_EVAL, LayerDescriptor(jsHighlighter, ""))
        registerLayer(T_EVAL_EXPRESSION, LayerDescriptor(jsHighlighter, ";"))

        val outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(CuteLanguage.getDefaultTemplateLang(), project, virtualFile)
        registerLayer(T_DATA, LayerDescriptor(outerHighlighter, ""))
    }
}