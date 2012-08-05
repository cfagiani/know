package net.crfsol.know.core.service;

import net.crfsol.know.core.domain.Tag;


public interface TagService {

    public void saveTagTree(Tag tag);

    public Tag getTagTree();

    public Tag addTag(String label, String parent);

    public Tag findOrCreateTag(String label);
}
