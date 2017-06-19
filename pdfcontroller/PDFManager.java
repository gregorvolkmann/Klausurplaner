package pdfcontroller;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JTable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFManager {
	public PDFManager(){
		
	}
	public void exportPDF(String pfad, JTable calendarTable) throws FileNotFoundException, DocumentException{
		Document document = new Document(PageSize.A3.rotate());
		
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(pfad));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			cb.saveState();
			Graphics2D g2 = cb.createGraphicsShapes(1100, 830);

			Shape oldClip = g2.getClip();
			g2.clipRect(-500, -500, 4000, 4000);

			calendarTable.print(g2);
			g2.setClip(oldClip);

			g2.dispose();
			cb.restoreState();
		System.out.println("Doc close");
		document.close();
	}
}
