package org.cutejs.lang.psi

import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.psi.*

class CuteJSNamespaceReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element), PsiPolyVariantReference {
    private val key = element.text

    override fun multiResolve(incomplete: Boolean): Array<ResolveResult> {
        val project = element.project
        val namespace = CuteResolveUtil.findNamespaceDeclaration(project, key) ?: return emptyArray()
        val templateDeclaration = arrayOf(PsiElementResolveResult(namespace.ref) as ResolveResult)
        val expression = element as? JSReferenceExpressionImpl ?: return templateDeclaration

        return templateDeclaration.plus(CuteResolveUtil.findNamespaceGeneratedDeclaration(expression))
    }

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolve(): PsiElement? {
        return CuteResolveUtil.findNamespaceDeclaration(element.project, key)
    }
}