package net.crfsol.know.core.util;

import net.crfsol.know.core.util.LuceneUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/know-core/test-context.xml")
public class LuceneUtilTest {

    @Inject
    private LuceneUtil luceneUtil;

    @Test
    public void testCreate() throws Exception {
        luceneUtil.createDocument("12", "this is a test");


    }

    @Test
    public void testSearch() throws Exception {
        luceneUtil.createDocument("123", "Another tester");

      //  luceneUtil.search("tester",);
    }


}


