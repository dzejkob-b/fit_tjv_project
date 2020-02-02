package cz.cvut.fit.hrabajak.semestralka.rest;

import org.json.JSONArray;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.web.bind.annotation.RestController
public class RestRootController extends RestBase {

	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> index() {

		JSONArray msg = new JSONArray();

		msg.put("Semestralka REST api. You may use:");
		msg.put("'/product' - product controller");
		msg.put("'/orderrecord' - order record controller");

		return ResponseEntity.
				ok().
				body(msg.toString());
	}

}
