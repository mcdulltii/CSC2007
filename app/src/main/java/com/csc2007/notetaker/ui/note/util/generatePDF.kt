package com.csc2007.notetaker.ui.note.util

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.compose.ui.unit.sp
import java.io.ByteArrayOutputStream

fun generatePDF(context: Context, content: String): ByteArray {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas
    val paint = android.graphics.Paint().apply {
        textSize = 12.sp.value // Set text size in sp
    }

    val margin = 20f
    val availableWidth = pageInfo.pageWidth - (2 * margin)
    var yPos = margin + paint.textSize // Initial Y position with some padding

    // Split content into paragraphs based on '\n'
    val paragraphs = content.split("\n")

    for (paragraph in paragraphs) {
        var line = ""
        val words = paragraph.split("\\s+".toRegex())
        for (word in words) {
            val wordWidth = paint.measureText("$line $word")
            val spaceWidth = paint.measureText(" ") // Width of a space

            if (line.isEmpty()) {
                line += word
            } else if (wordWidth + spaceWidth > availableWidth) {
                canvas.drawText(line, margin, yPos, paint)
                yPos += paint.textSize // Move to the next line
                line = word
            } else {
                line += " $word"
            }

            // Check if the next line exceeds the page height, start a new page
            if (yPos + paint.textSize > pageInfo.pageHeight - margin) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPos = margin + paint.textSize // Reset Y position for new page
            }
        }

        // Draw the remaining line if not empty
        if (line.isNotEmpty()) {
            canvas.drawText(line, margin, yPos, paint)
            yPos += paint.textSize // Move to the next line
        }

        // Add extra space between paragraphs
        yPos += paint.textSize * 1.5f
    }

    pdfDocument.finishPage(page)

    val outputStream = ByteArrayOutputStream()
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()

    return outputStream.toByteArray()
}