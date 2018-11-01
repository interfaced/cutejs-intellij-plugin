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
    override fun getActionText(p0: DataContext?): String? {
        return null
    }

    override fun getGotoDeclarationTargets(element: PsiElement?, p1: Int, editor: Editor?): Array<PsiElement>? {
        val namespace = getParentOfType(element, CuteNamespaceImpl::class.java)
        val isChildOfNamespace = namespace != null
        val isChildOfExport = getParentOfType(element, CuteExportArgsImpl::class.java) != null
        val isChildOfTypedef = getParentOfType(element, CuteTypedefImpl::class.java) != null
        val isIdentifier = element?.node?.elementType == T_IDENTIFIER
        val isLastNamespaceIdentifier = namespace?.namespaceArgs?.expr?.lastChild == element

        if (!(isChildOfNamespace || isChildOfExport || isChildOfTypedef) || !isIdentifier) return null

        val cuteFile = element?.containingFile as CuteFile
        val cuteGeneratedFile = cuteFile.getOrFindGeneratedFile() ?: return null

        if (isChildOfNamespace && isLastNamespaceIdentifier && namespace?.namespaceArgs?.text == cuteGeneratedFile.namespace) {
            return arrayOf(cuteGeneratedFile.file.firstChild)
        } else if (isChildOfExport || isChildOfTypedef) {
            val identifier = element.node?.text ?: return null
            return CuteResolveUtil.findElementsInFile(identifier, cuteGeneratedFile.file)
        }

        return null
    }
}