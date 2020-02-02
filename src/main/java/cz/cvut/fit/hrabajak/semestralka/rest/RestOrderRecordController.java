package cz.cvut.fit.hrabajak.semestralka.rest;

import cz.cvut.fit.hrabajak.semestralka.data.OrderRecordFactory;
import cz.cvut.fit.hrabajak.semestralka.data.ProductFactory;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.*;
import org.json.JSONArray;
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
 * rest controller pro praci s objednavkou
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/orderrecord", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class RestOrderRecordController extends RestBase {

	@Autowired
	private OrderRecordFactory orderRecordFactory;
	@Autowired
	private ProductFactory productFactory;

	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> index() {

		JSONArray msg = new JSONArray();

		msg.put("Semestralka OrderRecord REST api. You may use:");
		msg.put("'/get/{code}' - get order record detail by code (GET)");
		msg.put("'/get/{code}/products' - get order products by code (GET)");
		msg.put("'/add/{code}/products' - add list of products (ids) with specified quantity to opened order (POST)");
		msg.put("'/remove/{code}/products' - remove list of products (ids) of specified quantity at opened order (DELETE)");
		msg.put("'/get/[opened|processing|finished|reversal]' - get list of opened orders specified by state (GET)");
		msg.put("'/set/{code}/[opened|processing|finished|reversal]' - change order state (PUT)");
		msg.put("'/delete/{code}' - delete order by code at reversal state (DELETE)");
		
		return ResponseEntity.
				ok().
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getAllOpened()).toString()).
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getAllProcessing()).toString()).
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getAllFinished()).toString()).
				header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getAllReversal()).toString()).
				body(msg.toString());
	}

	@RequestMapping(value = "/get/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> get(@PathVariable String code) {

		OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

		if (o != null) {
			return ResponseEntity.ok().body(OrderRecordDto.toDto.toResource(o));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Order with code `" + code + "` not found!"));
		}
	}

	@RequestMapping(value = "/get/{code}/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getProducts(@PathVariable String code) {

		OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

		if (o != null) {
			return ResponseEntity.ok().body(OrderRecordProductsDto.toDto.toResource(o));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Order with code `" + code + "` not found!"));
		}
	}

	@RequestMapping(value = "/add/{code}/products", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> updateProducts(@PathVariable String code, @RequestBody OrderAddDto adt) {

		OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

		if (o == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Order with code `" + code + "` not found!"));

		} else if (o.getStatus() != OrderRecord.Status.OPENED) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson("Order with code `" + code + "` is not at opened state! (`" + o.getStatus().toString() + "`)"));

		} else {

			for (OrderAddProduct ap : adt.getAddProducts()) {
				Product p = this.productFactory.getProduct(ap.getProduct_id());

				if (p == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Product with entity id `" + Long.toString(ap.getProduct_id()) + "` not found!"));

				} else {
					o.addProduct(p, ap.getQuantity());
				}
			}

			this.orderRecordFactory.saveOrder(o);

			return ResponseEntity.
					created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getProducts(o.getCode())).toUri()).
					body(OrderRecordProductsDto.toDto.toResource(o));
		}
	}

	@RequestMapping(value = "/remove/{code}/products", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> removeProducts(@PathVariable String code, @RequestBody OrderAddDto adt) {

		OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

		if (o == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Order with code `" + code + "` not found!"));

		} else if (o.getStatus() != OrderRecord.Status.OPENED) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson("Order with code `" + code + "` is not at opened state! (`" + o.getStatus().toString() + "`)"));

		} else {
			boolean anyRemoved = false;

			for (OrderAddProduct ap : adt.getAddProducts()) {
				Product p = this.productFactory.getProduct(ap.getProduct_id());

				if (p == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Product with entity id `" + Long.toString(ap.getProduct_id()) + "` not found!"));

				} else if (!o.removeProduct(p, ap.getQuantity())) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson("Product with entity id `" + Long.toString(ap.getProduct_id()) + "` not contained in order `" + o.getCode() + "`!"));

				} else {
					anyRemoved = true;
				}
			}

			if (!anyRemoved) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Nothing was removed from order with code `" + o.getCode() + "`!"));
			}

			this.orderRecordFactory.saveOrder(o);

			return ResponseEntity.
					created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getProducts(o.getCode())).toUri()).
					body(OrderRecordProductsDto.toDto.toResource(o));
		}
	}

	@RequestMapping(value = "/get/opened", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAllOpened() {
		return this.getAllByStatus(OrderRecord.Status.OPENED);
	}

	@RequestMapping(value = "/get/processing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAllProcessing() {
		return this.getAllByStatus(OrderRecord.Status.PROCESSING);
	}

	@RequestMapping(value = "/get/finished", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAllFinished() {
		return this.getAllByStatus(OrderRecord.Status.FINISHED);
	}

	@RequestMapping(value = "/get/reversal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getAllReversal() {
		return this.getAllByStatus(OrderRecord.Status.REVERSAL);
	}

	public HttpEntity<?> getAllByStatus(OrderRecord.Status status) {
		try {
			List<OrderRecord> items = this.orderRecordFactory.getAllByStatus(status);

			if (items.size() > 0) {
				// vystup je zde pouze zjednoduseny seznam

				return ResponseEntity.
						ok().
						body(OrderRecordDto.toSimpleDto.toResources(items));

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("No order records with state `" + status.toString() + "` found!"));
			}

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson(ex.getMessage()));
		}
	}

	@RequestMapping(value = "/set/{code}/opened", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> setOpened(@PathVariable String code) {
		return this.setOrderRecordStatus(code, OrderRecord.Status.OPENED);
	}

	@RequestMapping(value = "/set/{code}/processing", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> setProcessing(@PathVariable String code) {
		return this.setOrderRecordStatus(code, OrderRecord.Status.PROCESSING);
	}

	@RequestMapping(value = "/set/{code}/finished", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> setFinished(@PathVariable String code) {
		return this.setOrderRecordStatus(code, OrderRecord.Status.FINISHED);
	}

	@RequestMapping(value = "/set/{code}/reversal", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> setReversal(@PathVariable String code) {
		return this.setOrderRecordStatus(code, OrderRecord.Status.REVERSAL);
	}

	public HttpEntity<?> setOrderRecordStatus(String code, OrderRecord.Status status) {
		try {
			OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

			if (o == null) {
				return ResponseEntity.
						status(HttpStatus.NOT_FOUND).
						body(this.getErrorJson("No order records with code `" + code.toString() + "` found!"));

			} else if (!o.mayChangeStatus(status)) {
				return ResponseEntity.
						status(HttpStatus.INTERNAL_SERVER_ERROR).
						body(this.getErrorJson("Cannot change status from `" + o.getStatus().toString() + "` to `" + status.toString() + "` of order record with code `" + o.getCode() + "`!"));

			} else {
				o.setStatus(status);

				this.orderRecordFactory.saveOrder(o);

				return ResponseEntity.
						created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).get(o.getCode())).toUri()).
						body(OrderRecordDto.toDto.toResource(o));
			}

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson(ex.getMessage()));
		}
	}

	@RequestMapping(value = "/delete/{code}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> delete(@PathVariable String code) {

		OrderRecord o = this.orderRecordFactory.getOrderByCode(code);

		if (o == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getErrorJson("Order record with code `" + code + "` not found!"));

		} else if (o.getStatus() != OrderRecord.Status.REVERSAL) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorJson("Delete order record with state `" + o.getCode() + "` is not possible!"));

		} else {
			this.orderRecordFactory.deleteOrderRecord(o);

			return ResponseEntity.
					noContent().
					header(HttpHeaders.LINK, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getAllReversal()).withRel("listReversal").toString()).
					build();
		}
	}

}
