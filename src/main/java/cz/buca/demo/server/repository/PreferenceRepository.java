package cz.buca.demo.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import cz.buca.demo.server.entity.PreferenceEntity;

public interface PreferenceRepository  extends PagingAndSortingRepository<PreferenceEntity, Long> {

	List<PreferenceEntity> findByParentId(Long parentId);
	
	long deleteByParentId(Long parentId);
}