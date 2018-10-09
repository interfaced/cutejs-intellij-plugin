package org.cutejs.ide.codeInsight

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.cutejs.lang.psi.CuteReference

class CuteReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrator: PsiReferenceRegistrar) {
        registrator.registerReferenceProvider(JSPatterns.jsReferenceExpression(), object: PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                val text = element.text
                if (text.contains("templates")) {
                    return arrayOf(CuteReference(element, element.textRange))
                }

                return PsiReference.EMPTY_ARRAY
            }
        })
    }
}