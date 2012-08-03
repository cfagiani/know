package net.crfsol.know.resource;

import net.crfsol.know.core.domain.Resource;


public interface ResourceListener {

    public void resourceFound(Resource resource);

    public void scanComplete(String root);
}
