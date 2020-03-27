package cz.buca.demo.server.repository;

import org.springframework.data.domain.Page;

import cz.buca.demo.server.dto.Search;
import cz.buca.demo.server.dto.UserSearch;

public interface UserRepositoryCustom {

	Page<UserSearch> search(Search search);
}