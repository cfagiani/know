package net.crfsol.know.core.domain;

import java.util.List;


public class Tag {
    private String id;
    private String label;
    private String description;
    private Tag parent;
    private List<Tag> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Tag> getChildren() {
        return children;
    }

    public void setChildren(List<Tag> children) {
        this.children = children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public String printTree(int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append("\t");
        }
        builder.append(label).append("\n");
        if (getChildren() != null) {
            for (Tag t : getChildren()) {
                builder.append(t.printTree(depth + 1));
            }
        }
        return builder.toString();
    }


}
