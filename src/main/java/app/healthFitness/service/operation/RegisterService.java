package app.healthFitness.service.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.healthFitness.dto.appuser.AppUserRequestDTO;
import app.healthFitness.dto.appuser.AppUserResponseDTO;
import app.healthFitness.dto.role.RoleResponseDTO;
import app.healthFitness.exception.FitBuddyException;
import app.healthFitness.service.crud.AppUserCrudService;
import app.healthFitness.service.crud.RoleCrudService;

/**
 * Provides a service to handle the registration process.
 */
@Service
public class RegisterService {
	
	private final Logger logger;
	private final AppUserCrudService appUserCrudService;
	private final RoleCrudService roleCrudService;
	
	@Autowired
	public RegisterService(AppUserCrudService appUserCrudService, RoleCrudService roleCrudService) {
		this.appUserCrudService = appUserCrudService;
		this.roleCrudService = roleCrudService;
		this.logger = LoggerFactory.getLogger(RegisterService.class);
	}
	
	public Integer register(String name, String password) {
		// check if name already exists
		if (appUserCrudService.readByName(name) != null) {
			throw new FitBuddyException("Username already exists.");			
		}
		
		// check if default user role exists
		RoleResponseDTO userRoleDto = roleCrudService.readByName("USER"); 
		if (userRoleDto == null) {
			throw new FitBuddyException("Internal server error - default role doesn't exists.");
		}
		
		// create new app user
		AppUserRequestDTO appUserRequestDTO = new AppUserRequestDTO(name, password, userRoleDto.getName());
		
		AppUserResponseDTO newAppUserDto = appUserCrudService.create(appUserRequestDTO);
		
		logger.info("User registered: {}", newAppUserDto);
		
		return newAppUserDto.getId();
	}

}
