package org.cutejs.ide.codeInsight

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PatternCondition
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.cutejs.lang.psi.CuteJSNamespaceReference
import org.cutejs.lang.psi.CuteResolveUtil

class CuteReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrator: PsiReferenceRegistrar) {
        val pattern = JSPatterns.jsReferenceExpression().with(object : PatternCondition<JSReferenceExpression>("oneOfTemplates") {
            override fun accepts(referenceExpression: JSReferenceExpression, context: ProcessingContext): Boolean {
                val namespaces = CuteResolveUtil.getAllNamespaces(referenceExpression.project)
                return namespaces.contains(referenceExpression.text)
            }
        })

        registrator.registerReferenceProvider(pattern, object: PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                return arrayOf(CuteJSNamespaceReference(element))
            }
        })
    }
}