package com.spring.rest;

import java.util.List;

import lombok.Data;

@Data
public class Receipe {
	private Long id;
	private String name;
	private List<String> ingrediets;
}
