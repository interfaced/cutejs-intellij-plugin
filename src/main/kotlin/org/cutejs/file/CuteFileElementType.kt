package org.cutejs.file

import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import org.cutejs.lang.CuteLanguage
import org.cutejs.lang.stub.CuteFileStub

class CuteFileElementType : IStubFileElementType<CuteFileStub>(CuteLanguage.INSTANCE) {
    companion object {
        var INSTANCE: IFileElementType = CuteFileElementType()
    }
}