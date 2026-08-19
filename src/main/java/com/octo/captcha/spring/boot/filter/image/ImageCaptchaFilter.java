/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
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
package com.octo.captcha.spring.boot.filter.image;

import com.octo.captcha.spring.boot.filter.FilterConfigUtils;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;


import com.octo.captcha.module.jmx.JMXRegistrationHelper;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.ManageableCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;

/**
 * <p>Filter for image captcha filter processing.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class ImageCaptchaFilter implements Filter {
	public static final String JMX_REGISTERING_NAME = "com.octo.captcha.module.servlet:object=ImageCaptchaFilter";
	public static final String CSV_DELIMITER = ";";
	public static final String CAPTCHA_ERROR_URL_PARAMETER = "CaptchaErrorURL";
	public static final String CAPTCHA_RENDERING_URL_PARAMETER = "CaptchaRenderingURL";
	public static final String CAPTCHA_VERIFICATION_URLS_PARAMETER = "CaptchaVerificationURLs";
	public static final String CAPTCHA_FAIL_URLS_PARAMETER = "CaptchaFailURLs";
	public static final String CAPTCHA_QUESTION_NAME_PARAMETER = "CaptchaQuestionParameterName";
	public static final String CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER = "CaptchaChallengeResponseParameterName";
	public static final String CAPTCHA_SERVICE_CLASS_PARAMETER = "ImageCaptchaServiceClass";
	public static final String CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER = "RegisterToMBeanServer";

	private boolean captchaRegisterToMBeanServer = false;
	private static String captchaQuestionParameterName = null;
	private ImageCaptchaService captchaService = null;
	private static String captchaRenderingURL = null;
	private static String captchaErrorURL = null;
	private static String captchaChallengeResponseParameterName = null;
	protected Hashtable verificationForwards = new Hashtable();
	protected String captchaServiceClassName;

	/** Default constructor. */
	public ImageCaptchaFilter() {
	}

	/** Return the URL at which captcha images are rendered. @return the rendering URL */
	public static String getCaptchaRenderingURL() {
		return captchaRenderingURL;
	}

	/** Return the request parameter name used to expose the captcha question. @return the question parameter name */
	public static String getCaptchaQuestionParameterName() {
		return captchaQuestionParameterName;
	}

	/** Return the request parameter name carrying the captcha challenge response. @return the response parameter name */
	public static String getCaptchaChallengeResponseParameterName() {
		return captchaChallengeResponseParameterName;
	}

	/** Initialise the filter by reading its init parameters and instantiating the captcha service. @param theFilterConfig filter config @throws ServletException if a mandatory parameter is missing or the service cannot be created */
	@Override
	public void init(FilterConfig theFilterConfig) throws ServletException {
		captchaRenderingURL = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaRenderingURL", true);
		captchaErrorURL = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaErrorURL", true);
		String captchaVerificationURLs = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaVerificationURLs", true);
		String captchaForwardErrorURLs = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaFailURLs", true);
		StringTokenizer verificationURLs = new StringTokenizer(captchaVerificationURLs, ";", false);
		StringTokenizer forwardErrorURLs = new StringTokenizer(captchaForwardErrorURLs, ";", false);
		if (verificationURLs.countTokens() != forwardErrorURLs.countTokens()) {
			throw new ServletException("CaptchaVerificationURLs and CaptchaFailURLs values are not consistant in web.xml : there should be exactly one forward error for each verification URL !");
		} else {
			while(verificationURLs.hasMoreTokens()) {
				this.verificationForwards.put(verificationURLs.nextToken(), forwardErrorURLs.nextToken());
			}

			captchaQuestionParameterName = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaQuestionParameterName", true);
			captchaChallengeResponseParameterName = FilterConfigUtils.getStringInitParameter(theFilterConfig, "CaptchaChallengeResponseParameterName", true);
			this.captchaServiceClassName = FilterConfigUtils.getStringInitParameter(theFilterConfig, "ImageCaptchaServiceClass", true);
			this.captchaRegisterToMBeanServer = FilterConfigUtils.getBooleanInitParameter(theFilterConfig, "RegisterToMBeanServer", false);

			try {
				this.captchaService = (ImageCaptchaService)Class.forName(this.captchaServiceClassName).newInstance();
			} catch (InstantiationException var7) {
				InstantiationException e = var7;
				throw new CaptchaServiceException(e);
			} catch (IllegalAccessException var8) {
				IllegalAccessException e = var8;
				throw new CaptchaServiceException(e);
			} catch (ClassNotFoundException var9) {
				ClassNotFoundException e = var9;
				throw new CaptchaServiceException(e);
			}

			if (this.captchaRegisterToMBeanServer && this.captchaService instanceof ManageableCaptchaService) {
				ManageableCaptchaService manageable = (ManageableCaptchaService)this.captchaService;
				JMXRegistrationHelper.registerToMBeanServer(manageable, "com.octo.captcha.module.servlet:object=ImageCaptchaFilter");
			}

		}
	}

	/** Route the request to captcha rendering, verification or the normal filter chain based on the servlet path. @param theRequest servlet request @param theResponse servlet response @param theFilterChain filter chain @throws IOException if an I/O error occurs @throws ServletException if a servlet error occurs */
	@Override
	public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain theFilterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)theRequest;
		HttpServletResponse response = (HttpServletResponse)theResponse;
		StringBuffer servletPathBuff = (new StringBuffer()).append(request.getServletPath());
		if (request.getQueryString() != null) {
			servletPathBuff.append("?").append(request.getQueryString());
		}

		String servletPathInfo = servletPathBuff.toString();
		if (servletPathInfo.startsWith(captchaRenderingURL)) {
			try {
				this.generateAndRenderCaptcha(request, response);
			} catch (Throwable var9) {
				Throwable e = var9;
				response.sendRedirect(captchaErrorURL);
				e.printStackTrace();
			}
		} else if (this.verificationForwards.containsKey(servletPathInfo)) {
			this.verifyAnswerToACaptchaChallenge(request, response, servletPathInfo, theFilterChain);
		} else {
			this.addQuestionToRequest(request, response);
			theFilterChain.doFilter(theRequest, theResponse);
		}

	}

	/** Unregister the captcha service from the MBean server if it was previously registered. */
	@Override
	public void destroy() {
		if (this.captchaService instanceof ManageableCaptchaService && this.captchaRegisterToMBeanServer) {
			ManageableCaptchaService manageable = (ManageableCaptchaService)this.captchaService;
			JMXRegistrationHelper.unregisterFromMBeanServer("com.octo.captcha.module.servlet:object=ImageCaptchaFilter");
		}

	}

	/** Attach the captcha question for the current session to the request as an attribute. @param theRequest servlet request @param theResponse servlet response */
	private void addQuestionToRequest(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		String captchaID = theRequest.getSession().getId();
		String question = this.captchaService.getQuestionForID(captchaID, theRequest.getLocale());
		theRequest.setAttribute(getCaptchaQuestionParameterName(), question);
	}

	/** Generate a captcha challenge for the current session and write the image to the response as JPEG. @param theRequest servlet request @param theResponse servlet response @throws IOException if writing the image fails */
	private void generateAndRenderCaptcha(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
		String captchaID = theRequest.getSession().getId();
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

		try {
			BufferedImage challenge = this.captchaService.getImageChallengeForID(captchaID, theRequest.getLocale());
			/*JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
			jpegEncoder.encode(challenge);*/
			ImageIO.write(challenge, "jpeg", jpegOutputStream);
		} catch (CaptchaServiceException var9) {
			theResponse.sendError(404);
			return;
		}

		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		theResponse.setHeader("Cache-Control", "no-store");
		theResponse.setHeader("Pragma", "no-cache");
		theResponse.setDateHeader("Expires", 0L);
		theResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = theResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
	}

	/** Validate the captcha challenge response for the current session, forwarding to success or error accordingly. @param theRequest servlet request @param theResponse servlet response @param theVerificationURL the verification URL being processed @param theFilterChain filter chain @throws IOException if forwarding fails @throws ServletException if forwarding fails */
	private void verifyAnswerToACaptchaChallenge(HttpServletRequest theRequest, HttpServletResponse theResponse, String theVerificationURL, FilterChain theFilterChain) throws IOException, ServletException {
		String captchaID = theRequest.getSession().getId();
		String challengeResponse = theRequest.getParameter(captchaChallengeResponseParameterName);
		if (challengeResponse == null) {
			this.redirectError(theVerificationURL, theRequest, theResponse);
		} else {
			Boolean isResponseCorrect = Boolean.FALSE;

			try {
				isResponseCorrect = this.captchaService.validateResponseForID(captchaID, challengeResponse);
			} catch (CaptchaServiceException var9) {
			}

			if (isResponseCorrect) {
				this.forwardSuccess(theFilterChain, theRequest, theResponse);
			} else {
				this.redirectError(theVerificationURL, theRequest, theResponse);
			}

		}
	}

	/** Redirect to the configured error URL for the given verification URL after cleaning request attributes. @param theVerificationURL the verification URL being processed @param theRequest servlet request @param theResponse servlet response @throws ServletException if the redirect fails */
	private void redirectError(String theVerificationURL, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException {
		this.removeParametersFromRequest(theRequest);

		try {
			String forwardErrorURL = theRequest.getContextPath() + (String)this.verificationForwards.get(theVerificationURL);
			theResponse.sendRedirect(forwardErrorURL);
		} catch (IOException var5) {
			IOException e = var5;
			throw new ServletException(e);
		}
	}

	/** Continue the filter chain after a successful captcha validation, after cleaning request attributes. @param theFilterChain filter chain @param theRequest servlet request @param theResponse servlet response @throws ServletException if forwarding fails */
	private void forwardSuccess(FilterChain theFilterChain, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException {
		this.removeParametersFromRequest(theRequest);

		try {
			theFilterChain.doFilter(theRequest, theResponse);
		} catch (IOException var5) {
			IOException e = var5;
			throw new ServletException(e);
		}
	}

	/** Remove captcha-related attributes from the request. @param theRequest servlet request */
	private void removeParametersFromRequest(HttpServletRequest theRequest) {
		theRequest.removeAttribute(getCaptchaChallengeResponseParameterName());
		theRequest.removeAttribute(getCaptchaQuestionParameterName());
	}
}
