package org.cutejs.lang

import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.templateLanguages.SimpleTreePatcher
import org.cutejs.lang.psi.CuteTypes

class CuteInnerJSTreePatcher : SimpleTreePatcher() {
    override fun insert(parent: CompositeElement, anchorBefore: TreeElement?, toInsert: OuterLanguageElement) {
        if (anchorBefore != null && anchorBefore.elementType != CuteTypes.T_INNER_TEMPLATE_ELEMENT) {
            var topLevelElement = anchorBefore!!

            while (topLevelElement.treeParent !is FileElement && topLevelElement.treeParent.textRange.startOffset == anchorBefore.textRange.startOffset) {
                topLevelElement = topLevelElement.treeParent
            }

            topLevelElement.rawInsertBeforeMe(toInsert as TreeElement)
        } else if (anchorBefore != null) {
            var topLevelElement = parent

            while (topLevelElement !is FileElement && topLevelElement.textRange.endOffset == parent.textRange.endOffset) {
                topLevelElement = topLevelElement.treeParent
            }

            topLevelElement.rawAddChildren(toInsert as TreeElement)
        } else {
            super.insert(parent, anchorBefore, toInsert)
        }
    }
}