package cz.buca.demo.server.model;

import lombok.Data;

@Data
public class Search {

	private Integer page = 0;
	private Integer perPage = 20;
	private String sort;
	private String order = "ASC";
}