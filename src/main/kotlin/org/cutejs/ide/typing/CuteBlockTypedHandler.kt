package org.cutejs.ide.typing

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.psi.CuteFile

class CuteBlockTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (file !is CuteFile) return Result.CONTINUE
        val offset = editor.caretModel.offset

        val delimiters = Pair.create("{", "}}")
        val openBraceLength = (delimiters.first as String).length

        if (offset < openBraceLength) {
            return Result.CONTINUE
        }

        val previousChars = editor.document.getText(TextRange(offset - openBraceLength, offset))

        if (previousChars == delimiters.first) {
            val elementAt = file.viewProvider.findElementAt(offset, CuteLanguage.INSTANCE)

            if (elementAt != null && elementAt.text.contains("}}")) {
                return Result.CONTINUE
            }

            editor.document.insertString(offset, delimiters.second)
        }

        return Result.CONTINUE
    }
}