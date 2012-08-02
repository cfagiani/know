package net.crfsol.know.resource.filesystem.resolver;


import net.crfsol.know.core.domain.Resource;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;

import java.io.File;
import java.io.FileInputStream;

public class PowerpointResolver extends FileResolver {

    private static final String[] EXTENSIONS = {"ppt", "pptx"};
    private static final String TYPE = "POWERPOINT";

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
            if (file.getName().trim().endsWith("ppt")) {
                PowerPointExtractor extractor = new PowerPointExtractor(is);
                r.setTextContent(extractor.getText());
            } else {
                XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(new XSLFSlideShow(file.getAbsolutePath()));
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
