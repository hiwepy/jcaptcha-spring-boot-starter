/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.octo.captcha.spring.boot.ext.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.octo.captcha.module.filter.image.ImageCaptchaFilter;

/**
 * TODO
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class ExtendImageCaptchaFilter extends ImageCaptchaFilter {

	public static final String CAPTCHA_SERVICE_CLASS_PARAMETER = "ImageCaptchaServiceClass";
	public static final String CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER = "RegisterToMBeanServer";
	
	public ExtendImageCaptchaFilter() {
		super();
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	}
	
}
