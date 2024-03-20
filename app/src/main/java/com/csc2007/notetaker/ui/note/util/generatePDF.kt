package com.csc2007.notetaker.ui.note.util

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.compose.ui.unit.sp
import java.io.ByteArrayOutputStream


fun generatePDF(context: Context, content: String) : ByteArray {
    val pdfDocument = PdfDocument()

    // Create a page
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    // Convert String to bitmap and draw on page
    val canvas = page.canvas

    val paint = android.graphics.Paint()

    paint.textSize = 12.sp.value

    canvas.drawText(content, 50f, 50f, paint)

    // Finish drawing on the page
    pdfDocument.finishPage(page)

    val outputStream = ByteArrayOutputStream()

    pdfDocument.writeTo(outputStream)

    pdfDocument.close()

    return outputStream.toByteArray()

}