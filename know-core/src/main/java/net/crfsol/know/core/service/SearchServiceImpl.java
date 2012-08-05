package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.util.LuceneUtil;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * lucene backed implementation of the SearchService
 *
 * @author Christopher Fagiani
 */
public class SearchServiceImpl implements SearchService {

    @Inject
    private LuceneUtil luceneUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> search(String query) {
        return luceneUtil.resourceSearch(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource findResource(String loc) {
        try {
            return luceneUtil.getResourceByLocation(loc);
        } catch (Exception e) {
            //TODO: handle
        }
        return null;
    }
}
