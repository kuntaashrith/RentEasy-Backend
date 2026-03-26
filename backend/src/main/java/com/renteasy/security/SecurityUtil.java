package com.renteasy.security;

import com.renteasy.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
	private SecurityUtil() {
	}

	public static AuthUser requireAuthUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof AuthUser au)) {
			throw new ForbiddenException("Not authenticated");
		}
		return au;
	}
}

