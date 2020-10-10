package org.cutejs.ide.codeInsight

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.PsiElement
import org.cutejs.ide.icons.CuteIcons
import org.cutejs.lang.psi.CuteResolveUtil

class CuteLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
         if (element is JSReferenceExpression) {
             val text = element.text
             val project = element.project
             val templateDecl = CuteResolveUtil.findNamespaceDeclaration(project, text)
             if (templateDecl != null) {
                 val builder = NavigationGutterIconBuilder
                         .create(CuteIcons.ICON)
                         .setTarget(templateDecl.ref)
                         .setTooltipText("Navigate to template declaration")
                 result.add(builder.createLineMarkerInfo(element))
             }
         }
    }
}