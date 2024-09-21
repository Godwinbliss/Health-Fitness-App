package app.healthFitness.service.crud;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.healthFitness.dto.appuser.AppUserRequestDTO;
import app.healthFitness.dto.appuser.AppUserResponseDTO;
import app.healthFitness.dto.appuser.AppUserUpdateDTO;
import app.healthFitness.entity.AppUser;
import app.healthFitness.exception.FitBuddyException;
import app.healthFitness.repository.AppUserRepository;
import app.healthFitness.service.mapper.AppUserMapperService;

@Service
public class AppUserCrudService implements CrudService<AppUserRequestDTO, AppUserResponseDTO, AppUserUpdateDTO> {
	
	private final AppUserRepository appUserRepository;
	private final AppUserMapperService appUserMapperService;
	
	@Autowired
	public AppUserCrudService(AppUserRepository appUserRepository, AppUserMapperService appUserMapperService) {		
		this.appUserRepository = appUserRepository;
		this.appUserMapperService = appUserMapperService;
	}
	
	@Override
	public AppUserResponseDTO create(AppUserRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}
		if (appUserRepository.findByName(requestDTO.getName()).isPresent()) {
			throw new FitBuddyException("Username already exists.");
		}
		AppUser savedAppUser = appUserRepository.save(appUserMapperService.requestDtoToEntity(requestDTO));
		return appUserMapperService.entityToResponseDto(savedAppUser);
	}
	
	@Override
	public AppUserResponseDTO readById(Integer id) {		
		Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
		if (optionalAppUser.isEmpty()) {
			return null;
		}
		return appUserMapperService.entityToResponseDto(optionalAppUser.get());
	}
	
	public AppUserResponseDTO readByName(String name) {
		Optional<AppUser> optionalAppUser =	appUserRepository.findByName(name);
		if (optionalAppUser.isEmpty()) {
			return null;
		}
		return appUserMapperService.entityToResponseDto(optionalAppUser.get());
	}
	
	@NotNull	
	public List<AppUserResponseDTO> readAll() {
		return appUserMapperService.entitiesToResponseDtos((List<AppUser>) appUserRepository.findAll());
	}	
	
	@Override
	public AppUserResponseDTO update(Integer id, AppUserUpdateDTO updateDTO ) {
		if (updateDTO == null) {
			return null;
		}
		Optional<AppUser> optionalExistingAppUser = appUserRepository.findById(id);
		if (optionalExistingAppUser.isEmpty()) {
			return null;
		}
		if (!optionalExistingAppUser.get().getName().equals(updateDTO.getName()) &&
			appUserRepository.findByName(updateDTO.getName()).isPresent()) {
				throw new FitBuddyException("Username already exists.");			
		}
		AppUser savedAppUser = appUserRepository.save(
				appUserMapperService.applyUpdateDtoToEntity(optionalExistingAppUser.get(), updateDTO));		
		return appUserMapperService.entityToResponseDto(savedAppUser);
	}
	
	@Override
	public void delete(Integer id) {
		appUserRepository.deleteById(id);		
	}
	
}
