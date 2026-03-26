package com.renteasy.util;

import com.renteasy.dto.PageResponse;
import org.springframework.data.domain.Page;

public final class PageUtil {
	private PageUtil() {
	}

	public static <T> PageResponse<T> toPageResponse(Page<T> page) {
		return new PageResponse<>(
				page.getContent(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages()
		);
	}
}

