package com.radionow.stream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.radionow.stream.model.UserConfig;

public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {

	UserConfig findUserConfigByUserId(Long userId);

}
