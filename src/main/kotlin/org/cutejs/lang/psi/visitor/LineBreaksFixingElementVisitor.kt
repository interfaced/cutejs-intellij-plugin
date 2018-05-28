package org.cutejs.lang.psi.visitor

import com.intellij.psi.impl.source.tree.RecursiveTreeElementVisitor
import com.intellij.psi.impl.source.tree.TreeElement
import java.util.LinkedList
import com.intellij.psi.impl.source.tree.Factory
import com.intellij.psi.impl.source.tree.LeafElement


class LineBreaksFixingElementVisitor(private val offsets: LinkedList<Int>) : RecursiveTreeElementVisitor() {
    private var shift: Int = 0

    override fun visitNode(element: TreeElement?): Boolean {
        return true
    }

    override fun visitLeaf(leaf: LeafElement) {
        if (offsets.isEmpty() || shift + leaf.textOffset + leaf.textLength < offsets.peekFirst()) {
            return
        }

        while (!offsets.isEmpty() && offsets.peekFirst() < shift + leaf.textOffset) {
            offsets.pollFirst()
        }

        val newText = StringBuilder(leaf.text)
        var localShift = 0

        while (!offsets.isEmpty() && offsets.peekFirst() < shift + leaf.textOffset + leaf.textLength) {
            val index = offsets.pollFirst() - (shift + localShift + leaf.textOffset)
            newText.deleteCharAt(index)
            localShift++
        }

        shift += localShift

        if (newText.isNotEmpty()) {
            val newAnchor = Factory.createSingleLeafElement(leaf.elementType, newText, 0, newText.length, null, leaf.manager)

            leaf.rawInsertBeforeMe(newAnchor)
        }

        leaf.rawRemove()
    }
}