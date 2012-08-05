package net.crfsol.know.resource.filesystem.resolver;

import net.crfsol.know.core.domain.Resource;

import java.io.File;


/**
 * classes that implement this interface are capable of parsing a File and returning a fully hydrated Resource instance.
 *
 * @author Christopher Fagiani
 */
public interface ResourceResolver {

    /**
     * returns true if this ResourceResolve instance is capable of parsing the file based on the file extension.
     *
     * @param extension
     * @return
     */
    public boolean accepts(String extension);

    /**
     * returns the Resource obtained by parsing the file.
     *
     * @param file
     * @return
     */
    public Resource resolveResource(File file);

}
