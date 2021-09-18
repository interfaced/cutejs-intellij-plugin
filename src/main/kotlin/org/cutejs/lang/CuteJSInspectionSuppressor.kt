package org.cutejs.lang

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.lang.javascript.inspections.ES6UnusedImportsInspection
import com.intellij.psi.PsiElement

class CuteJSInspectionSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, inspectionId: String): Boolean {
        // let CuteJSUnusedImportsInspection do his work
        return inspectionId == ES6UnusedImportsInspection.SHORT_NAME
    }

    override fun getSuppressActions(p0: PsiElement?, p1: String): Array<SuppressQuickFix> {
        return arrayOf()
    }
}