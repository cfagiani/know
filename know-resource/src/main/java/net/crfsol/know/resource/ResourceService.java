package net.crfsol.know.resource;

import java.util.Date;


/**
 * classes that implement this service are responsible for scanning a location passed in and reporting any resources found.
 * Resources can be optionally filtered by a date (i.e. only resources modified after the date specified will be returned).
 *
 * @author Christopher Fagiani
 */
public interface ResourceService {

    /**
     * scans a logical location for any resources modified since the sinceDate (if null, all resources at the location will be returned).
     * For each resource found, the resourceFound method will be called on the listener.
     *
     * @param location
     * @param sinceDate
     * @param listener
     */
    public void findResourcesAtLocation(String location, Date sinceDate, ResourceListener listener);
}
