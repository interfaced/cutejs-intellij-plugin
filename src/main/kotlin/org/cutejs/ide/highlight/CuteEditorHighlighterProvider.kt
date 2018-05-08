package org.cutejs.ide.highlight

import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.EditorHighlighterProvider
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class CuteEditorHighlighterProvider : EditorHighlighterProvider {
    override fun getEditorHighlighter(project: Project?,
                                      type: FileType,
                                      file: VirtualFile?,
                                      scheme: EditorColorsScheme): EditorHighlighter {
        return CuteLayeredHighlighter(project, file, scheme)
    }
}