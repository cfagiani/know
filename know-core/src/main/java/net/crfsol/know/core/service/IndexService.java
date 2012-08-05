package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;

import java.util.List;

/**
 * this service provides a layer on top of all indexing operations. When using batch operations, clients should take care
 * to call the flushBatch method when complete.
 *
 * @author Christopher Fagiani
 */
public interface IndexService {

    /**
     * this method will index a batch of resources. If autoFlushBatch is set to true, the index writer will be closed
     * upon completion of the batch. If the flag is false, clients must call flushBatch for the writer to be closed.
     *
     * @param resources
     * @param autoFinishBatch
     */
    public void indexBatch(List<Resource> resources, boolean autoFinishBatch);

    /**
     * indexes a single resource. This method will start and flush a batch automatically.
     *
     * @param resource
     */
    public void indexResource(Resource resource);

    /**
     * flushes an index batch by closing the writer.
     */
    public void flushBatch();

}
