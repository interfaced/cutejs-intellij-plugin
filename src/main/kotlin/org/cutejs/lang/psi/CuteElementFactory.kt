package org.cutejs.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.cutejs.lang.CuteFileType


class CuteElementFactory {
    companion object {
        fun createNamespaceIdentifier(project: Project, namespace: String): CuteNamespaceIdentifier {
            val file = createFile(project, "{{$$namespace}}")
            return file.templateNamespaceIdentifier()!!
        }

        fun createFile(project: Project, text: String): CuteFile {
            val name = "dummy.cute"
            val file = PsiFileFactory
                    .getInstance(project)
                    .createFileFromText(name, CuteFileType.INSTANCE, text)
            return file as CuteFile
        }
    }
}