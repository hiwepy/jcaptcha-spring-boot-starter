package com.octo.captcha.spring.boot;

import java.util.NoSuchElementException;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the JCaptcha integration, bound to the {@code jcaptcha.*} prefix.
 * <p>Selects the captcha delivery mode (servlet or filter) and configures captcha rendering/verification
 * URLs, store keys, timeout and filter init parameters.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = JCaptchaProperties.PREFIX)
public class JCaptchaProperties {

	public static final String PREFIX = "jcaptcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	/**
	 * Delivery mode for captcha challenges, resolved case-insensitively when binding properties.
	 */
	public enum JCaptchaType {

		FILTER("filter"), SERVLET("servlet");

		private final String jcaptchaType;

		JCaptchaType(String jcaptchaType) {
			this.jcaptchaType = jcaptchaType;
		}

		/** Return the string value of this captcha type. @return the type name */
		public String get() {
			return jcaptchaType;
		}

		/** Compare this type to another instance. @param jcaptchaType the type to compare @return true if equal */
		public boolean equals(JCaptchaType jcaptchaType) {
			return this.compareTo(jcaptchaType) == 0;
		}

		/** Compare this type to the type resolved from the given string. @param jcaptchaType the type name @return true if equal */
		public boolean equals(String jcaptchaType) {
			return this.compareTo(JCaptchaType.valueOfIgnoreCase(jcaptchaType)) == 0;
		}

		/** Resolve a captcha type case-insensitively by name. @param key the type name @return the matching enum */
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
	/** Cache key under which the captcha challenge is stored. */
	private String captchaStoreKey;
	/** Cache key under which the captcha creation timestamp is stored. */
	private String captchaDateStoreKey;
	/** Captcha validity period in milliseconds; default is 60000 (60 seconds). */
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
