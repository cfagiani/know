package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.util.LuceneUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * lucene-backed implementation of the index service. This service handles indexing any type of Resource.
 * This implementation requires the client to manage flushing batches
 * when indexing collections of resources.
 *
 * @author Christopher Fagiani
 */
public class IndexServiceImpl implements IndexService {
    @Inject
    private LuceneUtil luceneUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexBatch(List<Resource> resources, boolean autoFinishBatch) {
        if (resources != null && resources.size() > 0) {
            try {
                luceneUtil.startBatch();
                luceneUtil.indexResources(autoFinishBatch, resources);

            } catch (Exception e) {
                //TODO: handle
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushBatch() {
        try {
            luceneUtil.finishBatch();
        } catch (Exception e) {
            //TODO: handle
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexResource(Resource resource) {
        if (resource != null) {
            try {
                luceneUtil.startBatch();
                luceneUtil.indexResource(resource);
                luceneUtil.finishBatch();
            } catch (Exception e) {
                //TODO: handle
                e.printStackTrace();
            }
        }
    }
}
