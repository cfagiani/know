package net.crfsol.know.core.service;

import net.crfsol.know.core.domain.Tag;
import net.crfsol.know.core.util.LuceneUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Inject;


public class TagServiceImpl implements TagService {

    private static final String TAG_GUID = "89_TAG_GUID_TAG_XXX";

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
}
