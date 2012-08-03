package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class TextFileResolver extends FileResolver {
    private static final String[] EXTENSIONS = {"txt", "html", "java", "py", "jsp", "php", "h", "c", "cpp", "htm", "properties", "csv"};
    private static final String TYPE = "PLAINTEXT";

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
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            StringBuilder builder = new StringBuilder();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = reader.readLine();
            }
            r.setTextContent(builder.toString());
        } catch (Exception e) {
            //TODO: handle
        } finally {
            try {
                reader.close();
            } catch (Exception e) {

            }
        }
        return r;
    }
}
