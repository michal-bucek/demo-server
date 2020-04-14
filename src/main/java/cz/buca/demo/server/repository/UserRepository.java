package cz.buca.demo.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import cz.buca.demo.server.entity.UserEntity;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>, UserRepositoryCustom, JpaRepository<UserEntity, Long> {
		
	Optional<UserEntity> findByLogin(String login);
}