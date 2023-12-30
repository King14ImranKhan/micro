package com.appsdeveloperblog.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);

}
