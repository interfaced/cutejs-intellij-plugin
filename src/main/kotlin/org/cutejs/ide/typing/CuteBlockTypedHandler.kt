package org.cutejs.ide.typing

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.cutejs.lang.CuteFileType

import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.parser.CuteParserDefinition
import org.cutejs.lang.psi.CuteTypes

class CuteBlockTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType !in arrayOf(CuteFileType.INSTANCE, StdFileTypes.HTML)) return Result.CONTINUE

        val offset = editor.caretModel.offset

        val blockOpenSymbols = arrayOf(' ', '@', '$', '=', '-', '%', '*', '#')

        if (c in blockOpenSymbols) {
            val elementAt = file.viewProvider.findElementAt(offset, CuteLanguage.INSTANCE)

            if (elementAt != null && elementAt.node.elementType == CuteTypes.T_CLOSE && CuteParserDefinition.OPEN_MARKERS.contains(elementAt.parent.prevSibling.node.elementType)) {
                if (c != ' ') {
                    editor.document.insertString(offset, "  ")
                    editor.caretModel.moveToOffset(offset + 1)
                } else {
                    editor.document.insertString(offset, " ")
                    editor.caretModel.moveToOffset(offset)
                }

                return Result.CONTINUE
            }
        }

        val delimiters = Pair.create("{{", "}}")
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