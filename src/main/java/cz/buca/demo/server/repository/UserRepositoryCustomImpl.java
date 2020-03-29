package cz.buca.demo.server.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import cz.buca.demo.server.dto.user.SearchUser;
import cz.buca.demo.server.dto.user.UserSearch;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Page<UserSearch> search(SearchUser search) {
		Pageable pageable = null;
		
		if (search.getSort() != null && !search.getSort().equals("")) {
			String order = search.getOrder();
			
			if (order == null || order.equals("")) {
				order = Direction.ASC.toString();
			}
			
			pageable = PageRequest.of(search.getPage(), search.getPerPage(), Sort.by(Direction.fromString(order), search.getSort()));
			
		} else {
			pageable = PageRequest.of(search.getPage(), search.getPerPage());
		}
		
		System.out.println("pageable: "+ pageable);
		
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = pageable.getPageSize();
		String where = "";
		
		if (search.getText() != null && !search.getText().equals("")) {
			where  = " where user.name like '"+ search.getText() + "%'";
		}
		
		String orderBy = "";
		
		if (search.getSort() != null && !search.getSort().equals("")) {
			orderBy = " order by user."+ search.getSort() +" "+ search.getOrder();
		}
		
		String contentSql = "select new "+ UserSearch.class.getName() +"(user.id, user.name, user.login, user.email, user.modifier, user.modified) from User user" + where + orderBy;
		String totalSql = "select count(user) from User user" + where;
		
		System.out.println("contentSql: "+ contentSql);
		System.out.println("totalSql: "+ totalSql);
		System.out.println("firstResult: "+ firstResult);
		System.out.println("maxResults: "+ maxResults);
		
		List<UserSearch> content = entityManager
			.createQuery(contentSql, UserSearch.class)
			.setFirstResult(firstResult)
			.setMaxResults(maxResults)
			.getResultList();
		Long total = entityManager
			.createQuery(totalSql, Long.class)
			.getSingleResult();
		PageImpl<UserSearch> pageImpl = new PageImpl<UserSearch>(content, pageable, total);
		
		System.out.println("content: "+ content);
		System.out.println("total: "+ total);
		System.out.println("pageImpl: "+ pageImpl);
		System.out.println("pageImpl.getTotalElements: "+ pageImpl.getTotalElements());
		System.out.println("pageImpl.getNumber: "+ pageImpl.getNumber());
		System.out.println("pageImpl.getSize: "+ pageImpl.getSize());
		System.out.println("pageImpl.getContent: "+ pageImpl.getContent());
		
		return pageImpl;
	}	
}
