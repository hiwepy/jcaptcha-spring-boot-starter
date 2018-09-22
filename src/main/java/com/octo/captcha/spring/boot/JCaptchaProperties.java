package com.octo.captcha.spring.boot;

import java.util.NoSuchElementException;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = JCaptchaProperties.PREFIX)
public class JCaptchaProperties {

	public static final String PREFIX = "jcaptcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	public enum JCaptchaType {

		FILTER("filter"), SERVLET("servlet");

		private final String jcaptchaType;

		JCaptchaType(String jcaptchaType) {
			this.jcaptchaType = jcaptchaType;
		}

		public String get() {
			return jcaptchaType;
		}

		public boolean equals(JCaptchaType jcaptchaType) {
			return this.compareTo(jcaptchaType) == 0;
		}

		public boolean equals(String jcaptchaType) {
			return this.compareTo(JCaptchaType.valueOfIgnoreCase(jcaptchaType)) == 0;
		}

		public static JCaptchaType valueOfIgnoreCase(String key) {
			for (JCaptchaType jcaptchaType : JCaptchaType.values()) {
				if (jcaptchaType.get().equalsIgnoreCase(key)) {
					return jcaptchaType;
				}
			}
			throw new NoSuchElementException("Cannot found jcaptchaType with key '" + key + "'.");
		}

	}
	
	private JCaptchaType type = JCaptchaType.SERVLET;
	/**
	 * 验证码缓存的key
	 */
	private String captchaStoreKey;
	/**
	 * 验证码创建时间缓存的key
	 */
	private String captchaDateStoreKey;
	/**
	 * 验证码有效期；单位（毫秒），默认 60000
	 */
	private long captchaTimeout = DEFAULT_CAPTCHA_TIMEOUT;

	private String captchaServletPattern = "/jcaptcha.jpg";

	private String captchaFilterPattern = "/jcaptcha";
	private boolean captchaRegisterToMBeanServer;
	private String captchaQuestionParameterName = null;
	private String captchaRenderingURL = null;
	private String captchaErrorURL = null;
	private String captchaChallengeResponseParameterName = null;
	private String captchaVerificationURLs = "";
	private String captchaForwardErrorURLs = "";

	public JCaptchaType getType() {
		return type;
	}

	public void setType(JCaptchaType type) {
		this.type = type;
	}

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}

	public void setCaptchaStoreKey(String captchaStoreKey) {
		this.captchaStoreKey = captchaStoreKey;
	}

	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}

	public void setCaptchaDateStoreKey(String captchaDateStoreKey) {
		this.captchaDateStoreKey = captchaDateStoreKey;
	}

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}

	public void setCaptchaTimeout(long captchaTimeout) {
		this.captchaTimeout = captchaTimeout;
	}
	

	public String getCaptchaServletPattern() {
		return captchaServletPattern;
	}

	public void setCaptchaServletPattern(String captchaServletPattern) {
		this.captchaServletPattern = captchaServletPattern;
	}

	public boolean isCaptchaRegisterToMBeanServer() {
		return captchaRegisterToMBeanServer;
	}

	public void setCaptchaRegisterToMBeanServer(boolean captchaRegisterToMBeanServer) {
		this.captchaRegisterToMBeanServer = captchaRegisterToMBeanServer;
	}

	public String getCaptchaQuestionParameterName() {
		return captchaQuestionParameterName;
	}

	public void setCaptchaQuestionParameterName(String captchaQuestionParameterName) {
		this.captchaQuestionParameterName = captchaQuestionParameterName;
	}

	public String getCaptchaRenderingURL() {
		return captchaRenderingURL;
	}

	public void setCaptchaRenderingURL(String captchaRenderingURL) {
		this.captchaRenderingURL = captchaRenderingURL;
	}

	public String getCaptchaErrorURL() {
		return captchaErrorURL;
	}

	public void setCaptchaErrorURL(String captchaErrorURL) {
		this.captchaErrorURL = captchaErrorURL;
	}

	public String getCaptchaChallengeResponseParameterName() {
		return captchaChallengeResponseParameterName;
	}

	public void setCaptchaChallengeResponseParameterName(String captchaChallengeResponseParameterName) {
		this.captchaChallengeResponseParameterName = captchaChallengeResponseParameterName;
	}

	public String getCaptchaVerificationURLs() {
		return captchaVerificationURLs;
	}

	public void setCaptchaVerificationURLs(String captchaVerificationURLs) {
		this.captchaVerificationURLs = captchaVerificationURLs;
	}

	public String getCaptchaForwardErrorURLs() {
		return captchaForwardErrorURLs;
	}

	public void setCaptchaForwardErrorURLs(String captchaForwardErrorURLs) {
		this.captchaForwardErrorURLs = captchaForwardErrorURLs;
	}

	public String getCaptchaFilterPattern() {
		return captchaFilterPattern;
	}

	public void setCaptchaFilterPattern(String captchaFilterPattern) {
		this.captchaFilterPattern = captchaFilterPattern;
	}
	

}
