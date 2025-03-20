package com.spring.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;

@Service
public class MarketService {

	private List<Product> products = new ArrayList<>();
	private List<Receipe> receipes = new ArrayList<>();
	private Long productID = 1L;
	private Long receipeID = 1L;

	public List<Product> getProducts() {
		return products;
	}

	public Product getProduct(Long id) {
		return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
	}

	public Product addProduct(Product p) {
		p.setId(productID++);
		p.setCreatedAt(LocalDateTime.now());
		p.setUpdatedAt(LocalDateTime.now());
		products.add(p);
		return p;
	}

	public Product updateProduct(Long id, Product product) {
		Product existingProduct = getProduct(id);
		products.removeIf(p -> p.getId().equals(id));
		product.setId(id);
		product.setCreatedAt(existingProduct.getCreatedAt());
		product.setUpdatedAt(existingProduct.getUpdatedAt());
		products.add(product);
		return product;
	}

	public void deleteProduct(Long id) {
		products.removeIf(p -> p.getId().equals(id));
		System.err.println("PRODUCT REMOVED SUCCESFULLY: " + id);
	}

	public List<Product> getProductsByName(String name) {
		if (StringUtils.isBlank(name)) {
			return getProducts();
		}
		return products.stream().filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())).toList();
	}

	public List<Receipe> getReceipes() {
		return receipes;
	}

	public Receipe getReceipe(Long id) {
		return receipes.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
	}

	public Receipe addReceipe(Receipe r) {
		r.setId(receipeID++);
		receipes.add(r);
		return r;
	}

	public Receipe updateReceipe(Long id, Receipe receipe) {
		List<String> available = products.stream().map(Product::getName).toList();
		if (receipe.getIngrediets().stream().allMatch(available::contains)) {
			receipe.setId(receipeID++);
			receipes.add(receipe);
			return receipe;
		}
		return null;
	}

}
