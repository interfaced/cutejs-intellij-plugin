package org.cutejs.lang.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElement
import org.cutejs.ide.icons.CuteIcons
import java.util.ArrayList

class CuteReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {
    private val key = element.text

    override fun getVariants(): Array<Any> {
        val namespaces = CuteResolveUtil.findNamespaceDeclarations(element.project)
        val variants = ArrayList<LookupElement>()
        namespaces.forEach {
            variants.add(LookupElementBuilder.create(it)
                    .withIcon(CuteIcons.ICON)
                    .withTypeText(it.containingFile.name))
        }
        return variants.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        return CuteResolveUtil.findNamespaceDeclaration(element.project, key)
    }
}