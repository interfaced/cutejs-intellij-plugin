package org.cutejs.lang

import com.intellij.openapi.fileTypes.*

class CuteFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(CuteFileType.INSTANCE)
    }
}