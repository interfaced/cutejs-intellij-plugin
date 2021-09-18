package org.cutejs.ide.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration
import com.intellij.lang.javascript.inspections.ES6UnusedImportsInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.cutejs.lang.psi.CuteFile
import org.cutejs.lang.psi.impl.CuteRefImpl

class CuteJSUnusedImportsInspection : ES6UnusedImportsInspection() {
    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor {
        val originalVisitor = super.createVisitor(holder, session) as JSElementVisitor

        return object : JSElementVisitor() {
            override fun visitES6ImportDeclaration(importDeclaration: ES6ImportDeclaration?) {
                val cuteFile = importDeclaration?.containingFile?.viewProvider?.allFiles?.find { it is CuteFile }
                if (cuteFile == null) {
                    originalVisitor.visitES6ImportDeclaration(importDeclaration)
                    return
                }

                val refs = PsiTreeUtil.collectElementsOfType(cuteFile, CuteRefImpl::class.java)
                val isImportReferenced = refs.find { ref ->
                    importDeclaration.importedBindings.map { it.name }.contains(ref.name) ||
                            importDeclaration.importSpecifiers.map { it.alias?.name }.contains(ref.name) ||
                            importDeclaration.importSpecifiers.map { it.name }.contains(ref.name)
                } != null

                if (!isImportReferenced) {
                    originalVisitor.visitES6ImportDeclaration(importDeclaration)
                }
            }
        }
    }
}