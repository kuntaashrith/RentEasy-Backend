package com.renteasy.service;

import com.renteasy.dto.AuthLoginRequest;
import com.renteasy.dto.AuthRegisterRequest;
import com.renteasy.dto.AuthResponse;
import com.renteasy.dto.UserProfileResponse;
import com.renteasy.exception.BadRequestException;
import com.renteasy.model.User;
import com.renteasy.repository.UserRepository;
import com.renteasy.security.AuthUser;
import com.renteasy.security.JwtService;
import com.renteasy.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserMapper userMapper;

	@Transactional
	public AuthResponse register(AuthRegisterRequest req) {
		if (userRepository.existsByEmail(req.email().toLowerCase())) {
			throw new BadRequestException("Email already registered");
		}
		User user = User.builder()
				.name(req.name())
				.email(req.email().toLowerCase())
				.password(passwordEncoder.encode(req.password()))
				.phone(req.phone())
				.role(req.role())
				.build();
		userRepository.save(user);

		AuthUser authUser = AuthUser.from(user);
		String token = jwtService.generateAccessToken(authUser);
		UserProfileResponse profile = userMapper.toProfile(user);
		return new AuthResponse(token, "Bearer", jwtService.accessTokenExpiresInSeconds(), profile);
	}

	public AuthResponse login(AuthLoginRequest req) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password())
		);
		AuthUser user = (AuthUser) auth.getPrincipal();
		String token = jwtService.generateAccessToken(user);
		UserProfileResponse profile = userRepository.findById(user.getId())
				.map(userMapper::toProfile)
				.orElseThrow(() -> new BadRequestException("User not found"));
		return new AuthResponse(token, "Bearer", jwtService.accessTokenExpiresInSeconds(), profile);
	}
}

