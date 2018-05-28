package org.cutejs.file

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.lang.javascript.psi.resolve.JavaScriptResolveScopeProvider

class CuteElementResolveScopeProvider : JavaScriptResolveScopeProvider() {
    override fun getElementResolveScope(element: PsiElement): GlobalSearchScope? {
        return super.getElementResolveScope(element)
    }
}