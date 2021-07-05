package com.sierraobryan.example.customlint

import com.android.SdkConstants.IMAGE_BUTTON
import com.android.SdkConstants.IMAGE_VIEW
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.*
import org.w3c.dom.Element

class OnClickAccessibilityDetector : ResourceXmlDetector() {

    companion object {

        @JvmStatic
        val ISSUE = Issue.create(
            id = "OnClickAccessibilityDetector",
            briefDescription = "Use imageButton if imageView is clickable",
            explanation = "Consider using an imageButton in place of an imageView with onClickListener",
            category = Category.A11Y,
            severity = Severity.WARNING,
            implementation = Implementation(
                OnClickAccessibilityDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        // Return true if we want to analyze resource files in the specified resource
        // folder type. In this case we only need to analyze layout resource files.
        return folderType == ResourceFolderType.LAYOUT
    }

    override fun getApplicableAttributes(): Collection<String>? {
        // Return the set of attribute names we want to analyze. The `visitAttribute` method
        // below will be called each time lint sees one of these attributes in a
        // layout XML resource file. In this case, we want to analyze every attribute
        // in every layout XML resource file.
        return XmlScannerConstants.ALL
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(IMAGE_VIEW)
    }


    override fun visitElement(context: XmlContext, element: Element) {
        if (!element.hasAttribute("onClick"))
            return

        context.report(
            issue = ISSUE,
            scope = element,
            location = context.getLocation(element),
            message = "Use ImageButton with `onClick` instead",
            quickfixData = LintFix.create()
                .replace()
                // Put the text we're looking to replace
                .text(IMAGE_VIEW)
                // Put the text we want to replace it with.
                .with(IMAGE_BUTTON)
                .build()
        )
    }
}