package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;

import java.util.List;

public interface IndexService {

    public void indexBatch(List<Resource> resources, boolean autoFinishBatch);

    public void flushBatch();

}
