package net.crfsol.know.core.service;

import net.crfsol.know.core.domain.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: cfagiani
 * Date: 8/2/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/know-core/test-context.xml")
public class TagServiceTest {

    @Inject
    private TagService tagService;

    @Test
    public void testFindTree() {
        Tag tag = new Tag();
        tag.setLabel("root");
        tag.setChildren(createTagList("child", 3));
        tag.getChildren().get(0).setChildren(createTagList("grandchild", 5));
        tagService.saveTagTree(tag);

        Tag fromIndex = tagService.getTagTree();
        assertNotNull("Got null from index", fromIndex);
        assertEquals("Child list doesn't match", tag.getChildren().size(), fromIndex.getChildren().size());
    }

    private List<Tag> createTagList(String prefix, int count) {
        List<Tag> tagList = new ArrayList<Tag>();
        for (int i = 0; i < count; i++) {
            Tag t = new Tag();
            t.setLabel(prefix + i);
            tagList.add(t);
        }

        return tagList;
    }

}
