package net.crfsol.know.core.domain;

import java.util.List;


/**
 * tags provide the ability to classify documents. A tag has a globally unique label and can optionally have a parent
 * (thereby forming a tag tree).
 *
 * @author Christopher Fagiani
 */
public class Tag {
    private String label;
    private String description;
    private Tag parent;
    private List<Tag> children;

    public Tag(String label) {
        this.label = label;
    }

    public Tag() {
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

    /**
     * performs a pretty-print of a tree of tags. The depth parameter determines the number of tabs to prepend before
     * printing the label. If you want to print the tree from the root, call with depth = 0
     *
     * @param depth
     * @return
     */
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

    /**
     * equality is defined as having the same label
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (label != null ? !label.equals(tag.label) : tag.label != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    }
}
