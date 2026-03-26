package com.renteasy.service;

import com.renteasy.dto.PageResponse;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.dto.UserProfileResponse;
import com.renteasy.model.Property;
import com.renteasy.repository.PropertyImageRepository;
import com.renteasy.repository.PropertyRepository;
import com.renteasy.repository.UserRepository;
import com.renteasy.util.PageUtil;
import com.renteasy.util.PropertyMapper;
import com.renteasy.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserRepository userRepository;
	private final PropertyRepository propertyRepository;
	private final PropertyImageRepository propertyImageRepository;
	private final UserMapper userMapper;
	private final PropertyMapper propertyMapper;

	@Transactional(readOnly = true)
	public List<UserProfileResponse> allUsers() {
		return userRepository.findAll().stream().map(userMapper::toProfile).toList();
	}

	@Transactional(readOnly = true)
	public PageResponse<PropertyListItemResponse> allProperties(Pageable pageable) {
		Page<Property> page = propertyRepository.findAll(pageable);
		Page<PropertyListItemResponse> mapped = page.map(p -> propertyMapper.toListItem(
				p,
				propertyImageRepository.findByPropertyId(p.getId()).stream().findFirst().map(img -> img.getImageUrl()).orElse(null)
		));
		return PageUtil.toPageResponse(mapped);
	}
}

