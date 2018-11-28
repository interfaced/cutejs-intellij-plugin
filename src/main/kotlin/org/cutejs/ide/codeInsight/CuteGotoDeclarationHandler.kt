package org.cutejs.ide.codeInsight

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import org.cutejs.lang.psi.CuteFile
import org.cutejs.lang.psi.CuteResolveUtil
import org.cutejs.lang.psi.CuteTypes.T_IDENTIFIER
import org.cutejs.lang.psi.impl.*

class CuteGotoDeclarationHandler : GotoDeclarationHandler {
    override fun getGotoDeclarationTargets(element: PsiElement?, p1: Int, editor: Editor?): Array<PsiElement>? {
        val ref = getParentOfType(element, CuteRefImpl::class.java)
        val isChildOfExport = getParentOfType(element, CuteExportArgsImpl::class.java) != null
        val isChildOfTypedef = getParentOfType(element, CuteTypedefImpl::class.java) != null
        val isIdentifier = element?.node?.elementType == T_IDENTIFIER
        val isLastRefIdentifier = when {
            ref != null -> ref.expr.lastChild == element
            else -> false
        }

        if (!isIdentifier) return null

        if (isChildOfExport || isChildOfTypedef) {
            val cuteFile = element?.containingFile as CuteFile
            val cuteGeneratedFile = cuteFile.getOrFindGeneratedFile() ?: return null

            val identifier = element.node?.text ?: return null
            return CuteResolveUtil.findElementsInFile(identifier, cuteGeneratedFile.file)
        }

        if (ref != null && isLastRefIdentifier) {
            val resolved = CuteResolveUtil.resolveReference(ref) ?: return null
            return arrayOf(resolved)
        }

        return null
    }
}