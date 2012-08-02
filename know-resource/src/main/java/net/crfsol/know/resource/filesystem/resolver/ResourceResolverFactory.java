package net.crfsol.know.resource.filesystem.resolver;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ResourceResolverFactory {

    private List<ResourceResolver> resolvers;

    public void setResolvers(List<ResourceResolver> resolvers) {
        this.resolvers = resolvers;
    }

    private ResourceResolver defaultResolver = new FileResolver();


    public ResourceResolver getResourceResolver(File file) {
        if (file != null) {
            String fileName = file.getName();
            fileName = fileName.trim().toLowerCase();
            if (fileName.contains(".") && !fileName.endsWith(".")) {
                String extension = fileName.substring(fileName.lastIndexOf(".")+1);
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
