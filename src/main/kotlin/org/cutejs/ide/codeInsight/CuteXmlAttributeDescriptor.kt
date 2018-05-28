package org.cutejs.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.xml.impl.XmlAttributeDescriptorEx
import com.intellij.xml.impl.BasicXmlAttributeDescriptor

class CuteXmlAttributeDescriptor(private val name: String) : BasicXmlAttributeDescriptor(), XmlAttributeDescriptorEx {
    override fun handleTargetRename(newTargetName: String): String? {
        return newTargetName
    }

    override fun isRequired(): Boolean {
        return false
    }

    override fun hasIdType(): Boolean {
        return false
    }

    override fun hasIdRefType(): Boolean {
        return false
    }

    override fun isEnumerated(): Boolean {
        return false
    }

    override fun getDeclaration(): PsiElement? {
        return null
    }

    override fun getName(): String {
        return name
    }

    override fun init(element: PsiElement) {
    }

    override fun getDependences(): Array<Any> {
        return arrayOf()
    }

    override fun isFixed(): Boolean {
        return false
    }

    override fun getDefaultValue(): String? {
        return null
    }

    override fun getEnumeratedValues(): Array<String>? {
        return arrayOf()
    }
}