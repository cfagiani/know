package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.util.LuceneUtil;

import javax.inject.Inject;
import java.util.List;

public class IndexServiceImpl implements IndexService {
    @Inject
    private LuceneUtil luceneUtil;

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

    @Override
    public void flushBatch() {
        try {
            luceneUtil.finishBatch();
        } catch (Exception e) {
            //TODO: handle
        }
    }
}
