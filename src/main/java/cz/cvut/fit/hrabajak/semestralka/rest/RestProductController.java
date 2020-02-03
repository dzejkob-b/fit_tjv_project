package cz.cvut.fit.hrabajak.semestralka.rest;

import cz.cvut.fit.hrabajak.semestralka.data.ProductFactory;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.ProductDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * rest controller pro praci s katalogem produktu
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/product", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class RestProductController extends RestBase {

	private static int pagingLength = 5;

	@Autowired
	private ProductFactory productFactory;

	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> index() {

		JSONArray msg = new JSONArray();

		msg.put("Semestralka Product REST api. You may use:");
		msg.put("'/get/{name}' - get product by name (GET)");
		msg.put("'/getid/{id}' - get product by entity id (GET)");
		msg.put("'/getall[/{page}]' - display products (at specified {page}) (GET)");
		msg.put("'/delete/{id}' - delete product with entity id (DELETE)");
		msg.put("'/update' - update or create product (POST)");

		return ResponseEntity.
				ok().
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getAllFirst()).toString()).
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).update(null)).toString()).
				body(msg.toString());
	}

	@RequestMapping(value = "/get/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> get(@PathVariable String name) {

		Product p = this.productFactory.getProduct(name);

		if (p != null) {
			return ResponseEntity.ok().body(ProductDto.toDto.toResource(p));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Product with name `" + name + "` not found!", HttpStatus.NOT_FOUND));
		}
	}

	@RequestMapping(value = "/getid/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getId(@PathVariable long id) {

		Product p = this.productFactory.getProduct(id);

		if (p != null) {
			return ResponseEntity.ok().body(ProductDto.toDto.toResource(p));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Product with id `" + Long.toString(id) + "` not found!", HttpStatus.NOT_FOUND));
		}
	}

	@RequestMapping(value = "/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAllFirst() {
		return this.getAll(0);
	}

	@RequestMapping(value = "/getall/{page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAll(@PathVariable int page) {
		try {
			List<Product> items = this.productFactory.getAllProducts(page * RestProductController.pagingLength, RestProductController.pagingLength);

			if (items.size() > 0) {
				return ResponseEntity.
						ok().
						header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getAll(page + 1)).withRel("next").toString()).
						body(ProductDto.toDto.toResources(items));
				
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("No more products at page `" + Integer.toString(page) + "`", HttpStatus.NOT_FOUND));
			}

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> update(@RequestBody ProductDto dt) {
		try {
			Product p = null;

			if (dt.getEntity_id() != 0) {
				p = this.productFactory.getProduct(dt.getEntity_id());
			} else {
				p = this.productFactory.getProduct(dt.getName());
			}

			if (p == null) {
				p = new Product();
			}

			p.setName(dt.getName());
			p.setPrice(dt.getPrice());

			this.productFactory.saveProduct(p);
			
			return ResponseEntity.
					created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getId(p.getId())).toUri()).
					body(ProductDto.toDto.toResource(p));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> delete(@PathVariable long id) {

		Product p = this.productFactory.getProduct(id);

		if (p != null) {

			this.productFactory.deleteProduct(p);

			return ResponseEntity.
					noContent().
					header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getAllFirst()).withRel("list").toString()).
					build();

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Product with id `" + Long.toString(id) + "` not found!", HttpStatus.NOT_FOUND));
		}
	}

}
