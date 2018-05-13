package org.cutejs.ide.codeInsight

import com.intellij.psi.html.HtmlTag
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

class CuteAttributeDescriptorsProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(xmlTag: XmlTag): Array<XmlAttributeDescriptor> {
        if (xmlTag is HtmlTag) {
            val result = LinkedHashMap<String, XmlAttributeDescriptor>()
            for (attr in CUTE_ATTRIBUTES) {
                result[attr] = CuteXmlAttributeDescriptor(attr)
            }
            return result.values.toTypedArray()
        }
        return XmlAttributeDescriptor.EMPTY
    }

    override fun getAttributeDescriptor(attrName: String, xmlTag: XmlTag?): XmlAttributeDescriptor? {
        if (xmlTag != null) {
            if (CUTE_ATTRIBUTES.contains(attrName)) {
                return CuteXmlAttributeDescriptor(attrName)
            }
        }
        return null
    }

    companion object {
        val CUTE_ATTRIBUTES = arrayOf("data-export-id")
    }
}