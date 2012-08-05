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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * main engine class for the Know system. It is responsible for orchestrating calls between the major service subsystems (tagService,
 * searchService, indexService, etc). The engine backs both the CLI and Web interfaces.
 *
 * @author Christopher Fagiani
 */
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

    /**
     * adds a location to the resource service and installs itself as a listener to be notified when a new resource is discovered.
     *
     * @param loc
     */
    public void addResourceRoot(String loc) {
        resourceService.findResourcesAtLocation(loc, null, this);
    }


    /**
     * callback invoked by the resource service whenever a new resource is found. This method will simply add the resource to an internal
     * collection (used to batch-up resources before submitting to the index). If the internal collection is already > batch_size, the batch will be flushed.
     *
     * @param resource
     */
    @Override
    public void resourceFound(Resource resource) {
        resourceBatch.add(resource);
        if (resourceBatch.size() > BATCH_SIZE) {
            flushBatch();
        }
    }

    /**
     * callback invoked by the ResourceService after a location has been fully scanned. This method will simply flush the batch to ensure all resources have been indexed.
     *
     * @param root
     */
    @Override
    public void scanComplete(String root) {
        flushBatch();
        System.out.printf("Scan of %s complete\n", root);
        indexService.flushBatch();
    }

    /**
     * calls the indexBatch method on the indexService
     */
    private void flushBatch() {
        indexService.indexBatch(resourceBatch, false);
        resourceBatch.clear();
    }

    /**
     * returns the list of Resources obtained via passing the query to the searchService
     *
     * @param query
     * @return
     */
    public List<Resource> executeSearch(String query) {
        return searchService.search(query);
    }

    /**
     * adds a tag to the system taxonomy by calling the tagService.
     * TODO: remove printing
     *
     * @param tag
     * @param parent
     */
    public void addTag(String tag, String parent) {
        tagService.addTag(tag, parent);
        Tag tree = tagService.getTagTree();
        if (tree != null) {
            System.out.println(tree.printTree(0));
        }
    }

    /**
     * adds a tag to a resource, creating the tag if needed. This method assumes the resource exists in the index (if not, this method will have NO effect).
     *
     * @param tag
     * @param loc
     */
    public void tagResource(String tag, String loc) {
        if (tag != null && !tag.trim().isEmpty()) {
            Resource r = searchService.findResource(loc);
            if (r != null) {
                Tag t = tagService.findOrCreateTag(tag.trim());
                if (r.getTags() == null) {
                    Set<Tag> tags = new HashSet<Tag>();
                    r.setTags(tags);
                }
                r.getTags().add(t);
            }
            indexService.indexResource(r);
        }
    }
}
