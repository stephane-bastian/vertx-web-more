package com.thesoftwarefactory.vertx.web.more;

import io.vertx.ext.auth.User;

public interface UserContext extends User {

	public boolean isAuthenticated();
	
	public boolean hasAuthority(String authority);

}
