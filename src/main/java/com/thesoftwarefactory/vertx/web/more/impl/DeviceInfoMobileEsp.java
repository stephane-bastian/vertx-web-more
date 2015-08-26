package com.thesoftwarefactory.vertx.web.more.impl;

import com.handinteractive.mobile.UAgentInfo;
import com.thesoftwarefactory.vertx.web.more.DeviceInfo;

public class DeviceInfoMobileEsp implements DeviceInfo {

	private UAgentInfo userAgentInfo;
	
	public DeviceInfoMobileEsp(String userAgent, String httpAccept) {
		userAgentInfo = new UAgentInfo(userAgent, httpAccept);
	}

	@Override
	public boolean isPhone() {
		return userAgentInfo.detectTierIphone() || userAgentInfo.detectTierOtherPhones();
	}

	@Override
	public boolean isTablet() {
		return userAgentInfo.detectTierTablet();
	}

	@Override
	public boolean isDesktop() {
		return !isPhone() && !isTablet();
	}

	
}
