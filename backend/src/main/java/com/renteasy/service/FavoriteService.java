package com.renteasy.service;

import com.renteasy.dto.FavoriteResponse;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.exception.BadRequestException;
import com.renteasy.exception.ResourceNotFoundException;
import com.renteasy.model.FavoriteProperty;
import com.renteasy.model.Property;
import com.renteasy.model.User;
import com.renteasy.repository.FavoritePropertyRepository;
import com.renteasy.repository.PropertyImageRepository;
import com.renteasy.repository.PropertyRepository;
import com.renteasy.repository.UserRepository;
import com.renteasy.security.AuthUser;
import com.renteasy.security.SecurityUtil;
import com.renteasy.util.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
	private final FavoritePropertyRepository favoriteRepository;
	private final PropertyRepository propertyRepository;
	private final PropertyImageRepository propertyImageRepository;
	private final UserRepository userRepository;
	private final PropertyMapper propertyMapper;

	@Transactional
	public void add(Long propertyId) {
		AuthUser au = SecurityUtil.requireAuthUser();
		if (favoriteRepository.existsByUserIdAndPropertyId(au.getId(), propertyId)) {
			throw new BadRequestException("Already favorited");
		}
		User user = userRepository.findById(au.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		favoriteRepository.save(FavoriteProperty.builder().user(user).property(property).build());
	}

	@Transactional
	public void remove(Long propertyId) {
		AuthUser au = SecurityUtil.requireAuthUser();
		FavoriteProperty fav = favoriteRepository.findByUserIdAndPropertyId(au.getId(), propertyId)
				.orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
		favoriteRepository.delete(fav);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> list() {
		AuthUser au = SecurityUtil.requireAuthUser();
		List<FavoriteProperty> favs = favoriteRepository.findByUserId(au.getId());
		List<FavoriteResponse> out = new ArrayList<>(favs.size());
		for (FavoriteProperty f : favs) {
			Property p = f.getProperty();
			String thumb = propertyImageRepository.findByPropertyId(p.getId()).stream().findFirst().map(img -> img.getImageUrl()).orElse(null);
			PropertyListItemResponse prop = propertyMapper.toListItem(p, thumb);
			out.add(new FavoriteResponse(f.getId(), prop));
		}
		return out;
	}
}

