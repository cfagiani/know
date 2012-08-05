package net.crfsol.know.resource.filesystem;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.resource.ResourceListener;
import net.crfsol.know.resource.ResourceService;
import net.crfsol.know.resource.filesystem.resolver.ResourceResolverFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;

/**
 * filesystem implementation of the ResourceService. This class will recursively scan the filesystem starting at the path
 * passed in and will call the ResourceListener for each file found.
 *
 * @author Christopher Fagiani
 */
public class ResourceServiceFilesystemImpl implements ResourceService {

    @Inject
    private ResourceResolverFactory resolverFactory;


    /**
     * {@inheritDoc}
     *
     * @param location
     * @param sinceDate
     * @param listener
     */
    @Override
    public void findResourcesAtLocation(String location, Date sinceDate, ResourceListener listener) {
        findResourcesAtLocation(new File(location), sinceDate, listener);
        listener.scanComplete(location);
    }

    /**
     * recursively scans a directory for files. Hidden files/directories will be skipped (this assues hidden files start with a "." charater.
     *
     * @param root
     * @param sinceDate
     * @param listener
     */
    private void findResourcesAtLocation(File root, Date sinceDate, ResourceListener listener) {
        if (root.exists()) {
            if (root.isFile()) {
                resolveResource(root, sinceDate, listener);

            } else {
                for (File child : root.listFiles()) {
                    if (child.isDirectory()) {
                        findResourcesAtLocation(child, sinceDate, listener);
                    } else {
                        resolveResource(child, sinceDate, listener);
                    }
                }
            }
        }
    }

    /**
     * uses the resolverFactory to fully populate a Resource given a File. In this context, resolution includes parsing the file
     * to extract its content so it can eventually be indexed.
     *
     * @param file
     * @param sinceDate
     * @param listener
     */
    private void resolveResource(File file, Date sinceDate, ResourceListener listener) {
        if (sinceDate == null || file.lastModified() > sinceDate.getTime() && !file.getName().startsWith(".")) {
            Resource r = resolverFactory.getResourceResolver(file).resolveResource(file);
            if (r != null) {
                listener.resourceFound(r);
            }
        }
    }
}
