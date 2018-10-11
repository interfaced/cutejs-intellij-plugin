package org.cutejs.lang.psi

import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSClassResolver.findElementsByNameIncludingImplicit
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.lang.javascript.psi.resolve.JSResolveUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.util.indexing.FileBasedIndex
import org.cutejs.index.CuteTemplateCacheIndex.Companion.TEMPLATE_CACHE_INDEX

class CuteResolveUtil {
    companion object {
        private const val GENERATED_EXTENSION = ".jst.js"

        fun findGeneratedFileByNamespace(namespace: String, project: Project): PsiFile? {
            val lastIdentifier = namespace.split(".").last()
            val projectScope = ProjectScope.getProjectScope(project)

            val fileElement = findElementsByNameIncludingImplicit(lastIdentifier, projectScope, false).firstOrNull {
                val fileName = it.containingFile.virtualFile.name
                val isJstGenFile = fileName.endsWith(GENERATED_EXTENSION)
                val elementNamespace = it.namespace?.qualifiedName
                val isTargetNamespace = elementNamespace != null && namespace == "$elementNamespace.$lastIdentifier"

                isJstGenFile && isTargetNamespace
            }

            return fileElement?.containingFile
        }

        fun findElementsInFile(identifier: String, file: PsiFile): Array<PsiElement>? {
            val scope = GlobalSearchScope.fileScope(file.project, file.virtualFile)
            val elements = findElementsByNameIncludingImplicit(identifier, scope, false)

            if (elements.isEmpty()) {
                return null
            }

            return elements.toTypedArray()
        }

        fun getAllNamespaces(project: Project): MutableCollection<String> {
            return FileBasedIndex.getInstance().getAllKeys(TEMPLATE_CACHE_INDEX, project)
        }

        fun findNamespaceGeneratedDeclaration(expression: JSReferenceExpressionImpl): Array<ResolveResult> {
            val jsResolveResults = JSResolveUtil.resolve(expression.containingFile, expression, object : JSReferenceExpressionResolver(expression, false) {
                override fun resolveFromProviders(): Array<ResolveResult>? = null
            }, false)

            return jsResolveResults
                    .filter { it.element?.containingFile?.name?.endsWith(GENERATED_EXTENSION) == true }
                    .toTypedArray()
        }

        fun findNamespaceDeclaration(project: Project, namespace: String): CuteNamespaceIdentifier? {
            val namespaces = getAllNamespaces(project)
            val fileIndex = FileBasedIndex.getInstance()
            val target = namespaces.find { namespace == it } ?: return null
            val virtualFile = fileIndex.getContainingFiles(TEMPLATE_CACHE_INDEX, target, ProjectScope.getProjectScope(project)).first()
            val cuteFile = PsiManager.getInstance(project).findFile(virtualFile) as? CuteFile ?: return null
            return cuteFile.templateNamespaceIdentifier()
        }
    }
}