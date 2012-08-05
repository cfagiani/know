package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

/**
 * populates a resource by parsing the contents of a Word file (doc and docx) using Apache POI
 *
 * @author Christopher Fagiani
 */
public class WordResolver extends FileResolver {

    private static final String[] EXTENSIONS = {"doc", "docx"};
    private static final String TYPE = "WORD";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(String extension) {
        for (String e : EXTENSIONS) {
            if (e.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resolveResource(File file) {
        Resource r = super.resolveResource(file);
        r.getType().setCode(TYPE);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            if (file.getName().trim().endsWith("doc")) {
                WordExtractor extractor = new WordExtractor(is);
                r.setTextContent(extractor.getText());
            } else {
                XWPFWordExtractor extractor = new XWPFWordExtractor(new XWPFDocument(is));
                r.setTextContent(extractor.getText());
            }
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
