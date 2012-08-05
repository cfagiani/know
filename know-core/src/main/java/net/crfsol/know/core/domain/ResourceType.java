package net.crfsol.know.core.domain;


/**
 * Describes the type of resource. This can be something like WORD Doc, POWERPOINT, PDF, etc.
 *
 * @author Christopher Fagiani
 */
public class ResourceType {
    private String displayName;
    private String code;

    public ResourceType(String code) {
        this.code = code;
    }

    public ResourceType() {

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
