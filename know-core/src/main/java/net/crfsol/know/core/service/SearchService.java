package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;

import java.util.List;

public interface SearchService {

    public List<Resource> search(String query);
}
