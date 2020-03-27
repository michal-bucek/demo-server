package cz.buca.demo.server.dto;

import java.util.List;

import org.springframework.data.domain.Page;

@lombok.Data
public class Data<T> {

	private Long total;
	private Integer page;
	private Integer perPage;
	private List<T> items;
	
	public Data(Page<T> page) {
		this.total = page.getTotalElements();
		this.page = page.getNumber();
		this.perPage = page.getSize();
		this.items = page.getContent();
	}
}