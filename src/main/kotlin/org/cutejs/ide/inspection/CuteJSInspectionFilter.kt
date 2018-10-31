package org.cutejs.ide.inspection

import com.intellij.lang.javascript.highlighting.IntentionAndInspectionFilter
import com.intellij.codeInspection.InspectionProfileEntry
import com.intellij.lang.javascript.inspections.UnterminatedStatementJSInspection
import com.sixrr.inspectjs.validity.BadExpressionStatementJSInspection
import com.sixrr.inspectjs.validity.ThisExpressionReferencesGlobalObjectJSInspection

class CuteJSInspectionFilter : IntentionAndInspectionFilter() {
    private val unsupportedInspectionIDs = listOf(
            InspectionProfileEntry.getShortName(ThisExpressionReferencesGlobalObjectJSInspection::class.java.simpleName)
    )

    override fun isSupportedInspection(inspectionToolId: String?) = !unsupportedInspectionIDs.contains(inspectionToolId)
}