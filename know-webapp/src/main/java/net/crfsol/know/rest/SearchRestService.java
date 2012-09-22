package net.crfsol.know.rest;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.service.SearchService;
import net.crfsol.know.rest.dto.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Fagiani
 */
@Controller
@RequestMapping("/search")
public class SearchRestService {

    @Inject
    private SearchService searchService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public RestResponse<List<Resource>> search(@RequestParam(value="q", required = true) String query){
        List<Resource> resources = searchService.search(query);
        RestResponse<List<Resource>> response = new RestResponse<List<Resource>>();
        response.setPayload(resources);
        return response;
    }

}
