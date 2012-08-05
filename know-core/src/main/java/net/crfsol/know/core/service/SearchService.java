package net.crfsol.know.core.service;


import net.crfsol.know.core.domain.Resource;

import java.util.List;

/**
 * this service provides the search capabilities.
 *
 * @author Christopher Fagiani
 */
public interface SearchService {

    /**
     * returns a list of Resources that match the query passed it.
     *
     * @param query
     * @return
     */
    public List<Resource> search(String query);

    /**
     * finds a single Resource by its Location field.
     * @param loc
     * @return Resource if found, null if not.
     */
    public Resource findResource(String loc);
}
