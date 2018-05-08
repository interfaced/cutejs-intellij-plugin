package org.cutejs.ide.highlight

import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteTypes.*

class CuteLayeredHighlighter(project: Project?, virtualFile: VirtualFile?, colors: EditorColorsScheme) : LayeredLexerEditorHighlighter(CuteHighlighter(), colors) {
    init {
        var type: FileType? = null

        if (project == null || virtualFile == null) {
            type = StdFileTypes.PLAIN_TEXT
        } else {
            val language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
            if (language != null) {
                type = language.associatedFileType
            }
            if (type == null) {
                type = CuteLanguage.getDefaultTemplateLang()
            }
        }

        val jsHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(StdFileTypes.JS, project, virtualFile)
        registerLayer(T_TEMPLATE_JAVASCRIPT_CODE, LayerDescriptor(jsHighlighter, ";\n"))

        if (type != null) {
            val outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(type, project, virtualFile)
            registerLayer(T_TEMPLATE_HTML_CODE, LayerDescriptor(outerHighlighter, ""))
        }
    }
}