package org.cutejs.ide.codeInsight

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.lang.javascript.psi.JSPsiElementBase
import com.intellij.lang.javascript.psi.resolve.JSClassResolver.findElementsByNameIncludingImplicit
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import org.cutejs.lang.psi.CuteFile
import org.cutejs.lang.psi.CuteTypes.T_IDENTIFIER
import org.cutejs.lang.psi.impl.*



class CuteGotoDeclarationHandler : GotoDeclarationHandler {
    override fun getActionText(p0: DataContext?): String? {
        return null
    }

    override fun getGotoDeclarationTargets(element: PsiElement?, p1: Int, editor: Editor?): Array<PsiElement>? {
        val isChildOfNamespace = getParentOfType(element, CuteNamespaceImpl::class.java) != null
        val isChildOfExport = getParentOfType(element, CuteExportArgsImpl::class.java) != null
        val isChildOfTypedef = getParentOfType(element, CuteTypedefImpl::class.java) != null
        val isIdentifier = element?.node?.elementType == T_IDENTIFIER

        if (!(isChildOfNamespace || isChildOfExport || isChildOfTypedef) || !isIdentifier) return null

        val namespaceIdentifier = getParentOfType(element, CuteNamespaceIdentifierImpl::class.java)
        val project = editor?.project ?: return null
        val projectScope = ProjectScope.getProjectScope(project)
        val cuteFile = element?.containingFile as CuteFile
        val templateNamespace = namespaceIdentifier?.text ?: cuteFile.templateNamespace() ?: return null
        val templateIdentifier = templateNamespace.split(".").last()

        fun findByNameInNamespace(namespace: String, name: String, scope: GlobalSearchScope): Collection<JSPsiElementBase>  {
            return findElementsByNameIncludingImplicit(name, scope, false).filter {
                val fileName = it.containingFile.virtualFile.name
                val isJstGenFile = fileName.endsWith(".jst.js")
                val elementNamespace = it.namespace?.qualifiedName
                val isTargetNamespace = elementNamespace != null && namespace == "$elementNamespace.$name"

                isJstGenFile && isTargetNamespace
            }
        }

        val nsElements = findByNameInNamespace(templateNamespace, templateIdentifier, projectScope)

        if (isChildOfNamespace) {
            return nsElements.toTypedArray()
        } else if (isChildOfExport || isChildOfTypedef) {
            val identifier = element.node?.text ?: return null
            val files = nsElements.map { it.containingFile.virtualFile }
            val scope = GlobalSearchScope.filesScope(project, files)

            return findElementsByNameIncludingImplicit(identifier, scope, false).toTypedArray()
        }

        return null
    }
}