package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.domain.ResourceType;

import java.io.File;
import java.util.Date;


/**
 * base resolver for Files. By default, it simply creates a Resource with the title set to the filename and the location set to the absolute path.
 *
 * @author Christopher Fagiani
 */
public class FileResolver implements ResourceResolver {
    private static final String TYPE = "FILE";

    /**
     * * {@inheritDoc}
     */
    @Override
    public boolean accepts(String extension) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resolveResource(File file) {
        Resource r = new Resource();
        r.setLocation(file.getAbsolutePath());
        ResourceType type = new ResourceType();
        type.setCode(TYPE);
        r.setType(type);
        r.setLastModifiedDate(new Date(file.lastModified()));
        r.setName(file.getName());
        return r;
    }
}
