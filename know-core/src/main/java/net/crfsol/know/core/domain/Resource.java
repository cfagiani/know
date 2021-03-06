package net.crfsol.know.core.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * Datastructure to represent an indexed document. A resource can correspond to any indexable content. The location field
 * will provide enough information to retrieve the actual resource from its source (for the case of file resources, this will be the absolute path).
 *
 * @author Christopher Fagiani
 */
public class Resource {
    private String id;
    private String location;
    private String name;
    private String textContent;
    private String checksum;
    private Date lastIndexDate;
    private Date lastModifiedDate;
    private ResourceType type;
    private Set<Tag> tags;
    private Map<String, String> attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Date getLastIndexDate() {
        return lastIndexDate;
    }

    public void setLastIndexDate(Date lastIndexDate) {
        this.lastIndexDate = lastIndexDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getTagString() {
        StringBuilder builder = new StringBuilder();
        if (tags != null) {
            for (Tag t : tags) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(t.getLabel());
            }
        }
        return builder.toString();
    }
}
