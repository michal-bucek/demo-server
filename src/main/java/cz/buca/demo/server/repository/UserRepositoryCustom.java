package cz.buca.demo.server.repository;

import org.springframework.data.domain.Page;

import cz.buca.demo.server.model.user.SearchUser;
import cz.buca.demo.server.model.user.UserSearch;

public interface UserRepositoryCustom {

	Page<UserSearch> search(SearchUser search);
}