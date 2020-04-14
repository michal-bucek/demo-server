package cz.buca.demo.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.dto.preference.PreferenceDetail;
import cz.buca.demo.server.dto.preference.PreferenceSave;
import cz.buca.demo.server.entity.PreferenceEntity;
import cz.buca.demo.server.maper.DtoMapper;
import cz.buca.demo.server.repository.PreferenceRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PreferenceService {

	@Autowired
	private PreferenceRepository preferenceRepository;

	@Autowired
	private DtoMapper dtoMapper;

	public List<PreferenceDetail> findByParentId(Long parentId) {
		List<PreferenceDetail> details = preferenceRepository.findByParentId(parentId)
			.stream()
			.map(entity -> dtoMapper.toPreferenceDetail(entity))
			.collect(Collectors.toList());

		log.debug("find by parent ID return " + details + " for parent ID " + parentId);

		return details;
	}
	
	public PreferenceDetail save(PreferenceSave save) {
		PreferenceEntity oldEntity = dtoMapper.toPreferenceEntity(save);
		Long id = save.getId();
		
		if (id != null) {
			Optional<PreferenceEntity> optional = preferenceRepository.findById(id);
			
			if (!optional.isEmpty()) {
				oldEntity = optional.get();
				
				dtoMapper.updatePreferenceEntity(save, oldEntity);
			}
		}
		
		PreferenceEntity newEntity = preferenceRepository.save(oldEntity);
		PreferenceDetail detail = dtoMapper.toPreferenceDetail(newEntity);
		
		log.debug("save return "+ detail +" for "+ save);
		
		return detail;
	}
	
	public void deleteByParentId(Long parentId) {
		log.info("delete by parent ID with "+ parentId);		
		preferenceRepository.deleteByParentId(parentId);
	}
}