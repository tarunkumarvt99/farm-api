package com.spring.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/farm")
public class MarketController {

	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);

	private final MarketService service;

	public MarketController(MarketService service) {
		this.service = service;
	}

	@GetMapping("/products")
	public List<Product> getProducts(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		logger.info("Fetching products with name: {}, page: {}, size: {}", name, page, size);
		List<Product> filtered = service.getProductsByName(name);
		int start = Math.min(page * size, filtered.size());
		int end = Math.min((page + 1) * size, filtered.size());
		return filtered.subList(start, end);
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> getProduct(@PathVariable Long id) {
		logger.info("Fetching products with id: {}", id);
		Product p = service.getProduct(id);
		if (p != null)
			return ResponseEntity.ok(p);
		else {
			logger.warn("No such product found with id {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessage("No Product with id : " + id + " found."));
		}
	}

	@PostMapping("/products")
	public ResponseEntity<Product> addProduct(@Valid @RequestBody Product p) {
		logger.info("Adding product with name: {}", p.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.addProduct(p));
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product p) {
		logger.info("Updating product where id: {}, with name{}", id, p.getName());
//		Product existingProduct = service.getProduct(id);
//		return existingProduct != null ? ResponseEntity.ok(service.updateProduct(id, p))
//				: ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ErrorMessage("No Product with id : " + id + " to update."));
		Product updated = service.updateProduct(id, p);
		if (updated != null) {
			logger.info("Updated product with name: {}", updated.getName());
			return ResponseEntity.ok(updated);
		} else {
			logger.warn("Product with id {} not found to update", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessage("No Product with id : " + id + " to update."));
		}
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		logger.info("Deleting product where id: {}", id);
		Product product = service.getProduct(id);
		if (product != null) {
			service.deleteProduct(id);
			return ResponseEntity.noContent().build();
		}
		logger.warn("Product with id {} not found to delete", id);
		return ResponseEntity.notFound().build();
	}

}
