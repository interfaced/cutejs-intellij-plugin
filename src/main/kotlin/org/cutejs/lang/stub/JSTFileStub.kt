package org.cutejs.lang.stub

import com.intellij.psi.stubs.PsiFileStubImpl
import org.cutejs.lang.psi.CuteFile

class CuteFileStub(file: CuteFile) : PsiFileStubImpl<CuteFile>(file)