package mx.unam.aragon.view;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;

public class PageBackgroundEvent extends PdfPageEventHelper {
    private final Color backgroundColor;

    public PageBackgroundEvent(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void onEndPage(PdfWriter writer, com.lowagie.text.Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        Rectangle pageSize = document.getPageSize();

        canvas.setColorFill(backgroundColor);
        canvas.rectangle(pageSize.getLeft(), pageSize.getBottom(),
                pageSize.getWidth(), pageSize.getHeight());
        canvas.fill();
    }
}
