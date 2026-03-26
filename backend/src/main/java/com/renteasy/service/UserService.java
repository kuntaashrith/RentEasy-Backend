package com.renteasy.service;

import com.renteasy.dto.UserProfileResponse;
import com.renteasy.dto.UserUpdateRequest;
import com.renteasy.exception.ResourceNotFoundException;
import com.renteasy.model.User;
import com.renteasy.repository.UserRepository;
import com.renteasy.security.SecurityUtil;
import com.renteasy.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Transactional(readOnly = true)
	public UserProfileResponse getProfile() {
		Long userId = SecurityUtil.requireAuthUser().getId();
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return userMapper.toProfile(user);
	}

	@Transactional
	public UserProfileResponse updateProfile(UserUpdateRequest req) {
		Long userId = SecurityUtil.requireAuthUser().getId();
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		user.setName(req.name());
		user.setPhone(req.phone());
		return userMapper.toProfile(user);
	}
}

