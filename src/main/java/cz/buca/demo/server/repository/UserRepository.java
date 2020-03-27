package cz.buca.demo.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import cz.buca.demo.server.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserRepositoryCustom, JpaRepository<User, Long> {
		
	Optional<User> findByLogin(String login);
}