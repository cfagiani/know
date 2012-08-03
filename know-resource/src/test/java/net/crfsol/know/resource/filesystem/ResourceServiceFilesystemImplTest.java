package net.crfsol.know.resource.filesystem;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.resource.ResourceListener;
import net.crfsol.know.resource.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/know-resource/test-context.xml")
public class ResourceServiceFilesystemImplTest {

    @Inject
    private ResourceService resourceService;

    @Test
    public void findResourcesAtLocation() {
        resourceService.findResourcesAtLocation("/temp", null, new ResourceListener() {
            @Override
            public void scanComplete(String loc) {

            }

            @Override
            public void resourceFound(Resource resource) {
                assertNotNull("resource name is null", resource.getName());
                if (resource.getLocation().endsWith(".java")) {
                    assertNotNull("content is null for text file", resource.getTextContent());
                }
            }
        });
    }

    @Test
    public void findResourcesAtLocationSinceDate() {
        resourceService.findResourcesAtLocation("/temp", new Date(), new ResourceListener() {
            @Override
            public void scanComplete(String loc) {

            }

            @Override
            public void resourceFound(Resource resource) {
                assertTrue("Search should return nothing", false);
            }
        });
    }
}
