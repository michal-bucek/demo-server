package cz.buca.demo.server.dto;

import lombok.Data;

@Data
public class Search {

	private Integer page = 0;
	private Integer perPage = 20;
	private String sort;
	private String order = "ASC";
}