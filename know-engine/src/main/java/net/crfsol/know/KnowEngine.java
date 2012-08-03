package net.crfsol.know;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.domain.Tag;
import net.crfsol.know.core.service.IndexService;
import net.crfsol.know.core.service.SearchService;
import net.crfsol.know.core.service.TagService;
import net.crfsol.know.resource.ResourceListener;
import net.crfsol.know.resource.ResourceService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class KnowEngine implements ResourceListener {

    private static final int BATCH_SIZE = 100;

    @Inject
    private ResourceService resourceService;

    @Inject
    private SearchService searchService;

    @Inject
    private TagService tagService;

    @Inject
    private IndexService indexService;

    private List<Resource> resourceBatch;


    public KnowEngine() {
        resourceBatch = new ArrayList<Resource>();
    }

    public void addResourceRoot(String loc) {
        resourceService.findResourcesAtLocation(loc, null, this);
    }

    @Override
    public void resourceFound(Resource resource) {
        resourceBatch.add(resource);
        if (resourceBatch.size() > BATCH_SIZE) {
            flushBatch();
        }
    }

    @Override
    public void scanComplete(String root) {
        flushBatch();
        System.out.printf("Scan of %s complete\n", root);
        indexService.flushBatch();
    }

    private void flushBatch() {
        indexService.indexBatch(resourceBatch, false);
        resourceBatch.clear();
    }

    public List<Resource> executeSearch(String query) {
        return searchService.search(query);
    }

    public void addTag(String tag, String parent) {
        tagService.addTag(tag, parent);
        Tag tree = tagService.getTagTree();
        if (tree != null) {
            System.out.println(tree.printTree(0));
        }
    }
}
