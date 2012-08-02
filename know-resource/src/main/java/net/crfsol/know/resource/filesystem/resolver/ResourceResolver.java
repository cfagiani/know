package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;

import java.io.File;


public interface ResourceResolver {

    public boolean accepts(String extension);

    public Resource resolveResource(File file);

}
