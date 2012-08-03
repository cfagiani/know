package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;

public class PdfResolver extends FileResolver {

    private static final String[] EXTENSIONS = {"pdf"};
    private static final String TYPE = "PDF";

    @Override
    public boolean accepts(String extension) {
        for (String e : EXTENSIONS) {
            if (e.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Resource resolveResource(File file) {
        Resource r = super.resolveResource(file);
        r.getType().setCode(TYPE);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            PDFParser parser = new PDFParser(is);
            parser.parse();
            COSDocument cd = parser.getDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            r.setTextContent(stripper.getText(new PDDocument(cd)));
        } catch (Exception e) {
            //TODO: handle
        } finally {
            try {
                is.close();
            } catch (Exception e) {

            }
        }
        return r;
    }
}
