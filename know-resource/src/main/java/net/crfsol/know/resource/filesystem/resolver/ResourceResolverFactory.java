package net.crfsol.know.resource.filesystem.resolver;

import java.io.File;
import java.util.List;


/**
 * factory responsible for returning the correct ResourceResolver based on information about the file that represents the resource.
 *
 *
 * @author Christopher Fagiani
 */
public class ResourceResolverFactory {

    private List<ResourceResolver> resolvers;
    private ResourceResolver defaultResolver = new FileResolver();

    public void setResolvers(List<ResourceResolver> resolvers) {
        this.resolvers = resolvers;
    }


    /**
     * this method will use the file extension to find the correct ResolverInstance that is capable of parsing the file.
     * If no instance is found, the defaultResolver will be used.
     * @param file
     * @return
     */
    public ResourceResolver getResourceResolver(File file) {
        if (file != null) {
            String fileName = file.getName();
            fileName = fileName.trim().toLowerCase();
            if (fileName.contains(".") && !fileName.endsWith(".")) {
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                for (ResourceResolver r : resolvers) {
                    if (r.accepts(extension)) {
                        return r;
                    }
                }
            }
            return defaultResolver;
        } else {
            throw new IllegalArgumentException("File cannot be null");
        }

    }
}
