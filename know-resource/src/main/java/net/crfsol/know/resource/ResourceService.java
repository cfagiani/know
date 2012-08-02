package net.crfsol.know.resource;

import java.util.Date;


public interface ResourceService {

    public void findResourcesAtLocation(String location, Date sinceDate, ResourceListener listener);
}
