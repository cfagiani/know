package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.util.LuceneUtil;

import javax.inject.Inject;
import java.util.List;

public class SearchServiceImpl implements SearchService {

    @Inject
    private LuceneUtil luceneUtil;

    @Override
    public List<Resource> search(String query) {
        return luceneUtil.resourceSearch(query);
    }
}
