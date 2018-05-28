package org.cutejs.lang.psi.visitor

import com.intellij.lang.javascript.JSElementTypes
import com.intellij.lang.javascript.psi.impl.JSChangeUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.source.tree.RecursiveTreeElementVisitor
import com.intellij.psi.impl.source.tree.TreeElement

class LabeledStatementElementVisitor(private val project: Project) : RecursiveTreeElementVisitor() {
    override fun visitNode(element: TreeElement): Boolean {
        if (element.elementType == JSElementTypes.BLOCK_STATEMENT
                && element.findChildByType(JSElementTypes.LABELED_STATEMENT) != null) {
            val text = element.text
            val replacement = JSChangeUtil.createJSTreeFromText(project, "($text)")
            val objectLiteral = replacement.firstChildNode.findChildByType(JSElementTypes.OBJECT_LITERAL_EXPRESSION)
            replacement.firstChildNode.removeChild(replacement.firstChildNode.firstChildNode)
            replacement.firstChildNode.removeChild(replacement.firstChildNode.lastChildNode)

            if (objectLiteral != null) {
                element.rawInsertBeforeMe(objectLiteral as TreeElement)
                element.rawRemove()
            }
        }

        return true
    }
}