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

import cz.buca.demo.server.model.user.SearchUser;
import cz.buca.demo.server.model.user.UserSearch;

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
		
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = pageable.getPageSize();
		String where = " where 1 = 1";
		
		if (search.getName() != null && !search.getName().equals("")) {
			where = where + " and user.name like '"+ search.getName() + "%'";
		}
		
		if (search.getLogin() != null && !search.getLogin().equals("")) {
			where = where + " and user.login like '"+ search.getLogin() + "%'";
		}
		
		if (search.getEmail() != null && !search.getEmail().equals("")) {
			where = where + " and user.email like '"+ search.getEmail() + "%'";
		}

		if (search.getActive() != null) {
			where = where + " and user.active = "+ search.getActive();
		}
		
		String orderBy = "";
		
		if (search.getSort() != null && !search.getSort().equals("")) {
			orderBy = " order by user."+ search.getSort() +" "+ search.getOrder();
		}
		
		String contentSql = "select new "+ UserSearch.class.getName() +"(user.id, user.name, user.login, user.email, user.active, user.modifier, user.modified) from UserEntity user" + where + orderBy;
		String totalSql = "select count(user) from UserEntity user" + where;
		
		List<UserSearch> content = entityManager
			.createQuery(contentSql, UserSearch.class)
			.setFirstResult(firstResult)
			.setMaxResults(maxResults)
			.getResultList();
		Long total = entityManager
			.createQuery(totalSql, Long.class)
			.getSingleResult();
		PageImpl<UserSearch> pageImpl = new PageImpl<UserSearch>(content, pageable, total);
		
		return pageImpl;
	}	
}
