package net.crfsol.know.resource;

import net.crfsol.know.core.domain.Resource;


/**
 *
 * This interface is used by the ResourceService to notify clients as resources are discovered.
 *
 * @author Christopher Fagiani
 */
public interface ResourceListener {

    /**
     * callback that passes a newly discovered resource
     * @param resource
     */
    public void resourceFound(Resource resource);

    /**
     * called when a root has been fully scanned
     * @param root
     */
    public void scanComplete(String root);
}
