package net.crfsol.know.core.service;

import net.crfsol.know.core.domain.Tag;


public interface TagService {

    public void saveTagTree(Tag tag);

    public Tag getTagTree();
}
