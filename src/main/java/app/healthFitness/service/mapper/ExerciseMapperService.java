package app.healthFitness.service.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.healthFitness.dto.exercise.ExerciseRequestDTO;
import app.healthFitness.dto.exercise.ExerciseResponseDTO;
import app.healthFitness.dto.exercise.ExerciseUpdateDTO;
import app.healthFitness.entity.AppUser;
import app.healthFitness.entity.Exercise;
import app.healthFitness.exception.FitBuddyException;
import app.healthFitness.repository.AppUserRepository;

@Service
public class ExerciseMapperService implements MapperService<ExerciseRequestDTO, ExerciseResponseDTO,
															ExerciseUpdateDTO, Exercise> {
	
	private final AppUserRepository appUserRepository;
	
	@Autowired
	public ExerciseMapperService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public Exercise requestDtoToEntity(ExerciseRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}
		Optional<AppUser> optionalAppUser = appUserRepository.findById(requestDTO.getAppUserId());
		if (optionalAppUser.isEmpty()) {
			throw new FitBuddyException("AppUser not found with ID: " + requestDTO.getAppUserId());
		}		
		Exercise exercise = new Exercise();
		exercise.setName(requestDTO.getName());
		exercise.setAppUser(optionalAppUser.get());
		return exercise;		
	}

	@Override
	public ExerciseResponseDTO entityToResponseDto(Exercise entity) {
		if (entity == null) {
			return null;
		}
		return new ExerciseResponseDTO(entity.getId(), entity.getName(), entity.getAppUser().getId());
	}
	
	@Override
	public List<ExerciseResponseDTO> entitiesToResponseDtos(List<Exercise> entities) {
		if (entities == null || entities.isEmpty()) {
			return Collections.emptyList();
		}
		List<ExerciseResponseDTO> result = new ArrayList<>();
		for (Exercise entity : entities) {
			result.add(entityToResponseDto(entity));
		}
		return result;
	}

	@Override
	public Exercise applyUpdateDtoToEntity(Exercise entity, ExerciseUpdateDTO updateDTO) {
		if (entity == null || updateDTO == null) {
			return null;
		}
		if (updateDTO.getName() != null) {
			entity.setName(updateDTO.getName());
		}
		return entity;
	}
	
}
