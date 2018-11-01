package org.cutejs.index

import com.intellij.util.indexing.*
import com.intellij.util.io.KeyDescriptor
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.io.EnumeratorStringDescriptor
import gnu.trove.THashMap
import org.cutejs.lang.CuteFileType
import org.cutejs.lang.psi.CuteFile
import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.openapi.vfs.VirtualFile

class CuteTemplateCacheIndex : ScalarIndexExtension<String>() {
    private val myDataIndexer = MyDataIndexer()

    override fun getName(): ID<String, Void> = TEMPLATE_CACHE_INDEX

    override fun getVersion(): Int = 42

    override fun dependsOnFileContent(): Boolean = true

    override fun getIndexer(): DataIndexer<String, Void, FileContent> = myDataIndexer

    override fun getInputFilter(): FileBasedIndex.InputFilter = CuteTemplateIndexInputFilter.INSTANCE

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

    companion object {
        val TEMPLATE_CACHE_INDEX: ID<String, Void> = ID.create("cutejs.template.cache")
    }
}

class CuteTemplateIndexInputFilter : DefaultFileTypeSpecificInputFilter(CuteFileType.INSTANCE) {
    override fun acceptInput(file: VirtualFile): Boolean {
        return super.acceptInput(file) && file.fileSystem !is JarFileSystem
    }

    companion object {
        val INSTANCE = CuteTemplateIndexInputFilter()
    }
}

class MyDataIndexer : DataIndexer<String, Void, FileContent> {
    override fun map(fileContent: FileContent): MutableMap<String, Void> {
        val result = THashMap<String, Void>()
        val psiFile = fileContent.psiFile as? CuteFile ?: return result
        val namespace = psiFile.templateNamespace() ?: return result

        result[namespace.ref.text] = null
        return result
    }
}