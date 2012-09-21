package net.crfsol.know.rest;

import net.crfsol.know.core.domain.Tag;
import net.crfsol.know.core.service.TagService;
import net.crfsol.know.rest.dto.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Fagiani
 */
@Controller
@RequestMapping("/taxonomy")
public class TaxonomyRestService {

    @Inject
    private TagService tagService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public RestResponse<List<Tag>> getTagTree() {
        RestResponse<List<Tag>> response = new RestResponse<List<Tag>>();
        Tag root = tagService.getTagTree();
        if(root != null){
            response.setPayload(root.getChildren());
        }
        return response;
    }

}
