package net.crfsol.know.core.service;

import net.crfsol.know.core.domain.Tag;
import net.crfsol.know.core.util.LuceneUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class TagServiceImpl implements TagService {

    private static final String TAG_GUID = "89_TAG_GUID_TAG_XXX";
    private static final String ROOT_NAME = "KNOWLEDGEROOT";

    @Inject
    private LuceneUtil luceneUtil;
    @Inject
    private ObjectMapper mapper;

    private Tag tagTreeRoot;


    public Tag getTagTree() {
        if (tagTreeRoot == null) {
            try {
                String text = luceneUtil.getDocumentContentByGUID(TAG_GUID);
                if (text != null) {
                    tagTreeRoot = mapper.readValue(text, Tag.class);
                } else {
                    tagTreeRoot = new Tag();
                    tagTreeRoot.setLabel(ROOT_NAME);
                }
            } catch (Exception e) {
                //TODO: handle
                e.printStackTrace();
            }
        }
        return tagTreeRoot;
    }

    public void saveTagTree(Tag tag) {
        try {
            tagTreeRoot = tag;
            String content = mapper.writeValueAsString(tag);
            luceneUtil.createDocument(TAG_GUID, content);
        } catch (Exception e) {
            //todo: handle
            e.printStackTrace();

        }
    }

    @Override
    public Tag addTag(String label, String parent) {
        Tag root = getTagTree();
        Tag existingTag = findTag(root, label);
        if (existingTag != null) {
            return existingTag;
        } else {
            Tag parentTag = null;
            if (parent != null) {
                parentTag = findTag(root, parent);
                if (parentTag == null) {
                    //create parent
                    parentTag = new Tag();
                    parentTag.setLabel(parent);
                }
            } else {
                parentTag = root;
            }
            List<Tag> children = parentTag.getChildren();
            if (children == null) {
                children = new ArrayList<Tag>();
            }
            Tag child = new Tag();
            child.setLabel(label);
            children.add(child);
            parentTag.setChildren(children);
            saveTagTree(root);
            return child;
        }
    }

    public Tag findTag(Tag root, String label) {
        if (root != null) {
            if (root.getLabel().equalsIgnoreCase(label)) {
                return root;
            } else if (root.getChildren() != null) {
                for (Tag t : root.getChildren()) {
                    Tag found = findTag(t, label);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
}
