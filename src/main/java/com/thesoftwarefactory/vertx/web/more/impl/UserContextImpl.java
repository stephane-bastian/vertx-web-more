package com.thesoftwarefactory.vertx.web.more.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.thesoftwarefactory.vertx.web.more.UserContext;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;

public class UserContextImpl implements UserContext {

	private boolean isAuthenticated;
	private Set<Permission> permissions;
	private JsonObject principal;
	private Set<String> roles;
	private String userId;
	
	private final static UserContext anonymous = new UserContextImpl("anonymous", Collections.emptySet(), Collections.emptySet(), false);
	
	public final static UserContext anonymous() {
		return anonymous;
	}

	public UserContextImpl(String userId, Set<String> roles, Set<String> permissions, boolean isAuthenticated) {
		Objects.requireNonNull(userId);
		Objects.requireNonNull(roles);
		Objects.requireNonNull(permissions);
		
		this.roles = roles;
		this.permissions = new HashSet<>();
		for (String permission: permissions) {
			this.permissions.add(new WildcardPermission(permission));
		}
		this.isAuthenticated = isAuthenticated;
	}
	
	@Override
	public UserContext clearCache() {
		return this;
	}

	@Override
	public boolean hasAuthority(String authority) {
		if (authority!=null) {
			if (authority.startsWith("role:")) {
				return hasRole(authority.substring(5));
			}
			else {
				WildcardPermission thePermission = new WildcardPermission(authority);
				for (Permission permission: permissions) {
					if (permission.implies(thePermission)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean hasPermission(String permission) {
		WildcardPermission thePermission = new WildcardPermission(permission);
		for (Permission userPermission: permissions) {
			if (userPermission.implies(thePermission)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	@Override
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	@Override
	public UserContext isAuthorised(String authority, Handler<AsyncResult<Boolean>> resultHandler) {
		resultHandler.handle(Future.succeededFuture(hasAuthority(authority)));
		return this;
	}

	@Override
	public JsonObject principal() {
		if (principal==null) {
			principal = new JsonObject().put("username", userId);
		}
		return principal;
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		// noop
	}
	
}
