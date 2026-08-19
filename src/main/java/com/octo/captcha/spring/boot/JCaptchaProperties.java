package com.octo.captcha.spring.boot;

import java.util.NoSuchElementException;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = JCaptchaProperties.PREFIX)
public class JCaptchaProperties {

	public static final String PREFIX = "jcaptcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	/**
	 * Delivery mode for captcha challenges, resolved case-insensitively when binding properties.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
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
	/** Gets the type. */

	public JCaptchaType getType() {
		return type;
	}
	/** Sets the type. */

	public void setType(JCaptchaType type) {
		this.type = type;
	}
	/** Gets the captcha store key. */

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}
	/** Sets the captcha store key. */

	public void setCaptchaStoreKey(String captchaStoreKey) {
		this.captchaStoreKey = captchaStoreKey;
	}
	/** Gets the captcha date store key. */

	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}
	/** Sets the captcha date store key. */

	public void setCaptchaDateStoreKey(String captchaDateStoreKey) {
		this.captchaDateStoreKey = captchaDateStoreKey;
	}
	/** Gets the captcha timeout. */

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}
	/** Sets the captcha timeout. */

	public void setCaptchaTimeout(long captchaTimeout) {
		this.captchaTimeout = captchaTimeout;
	}
	/** Gets the captcha servlet pattern. */
	

	public String getCaptchaServletPattern() {
		return captchaServletPattern;
	}
	/** Sets the captcha servlet pattern. */

	public void setCaptchaServletPattern(String captchaServletPattern) {
		this.captchaServletPattern = captchaServletPattern;
	}
	/**
	 * <p>Is captcha register to m bean server.</p>
	 * @return the boolean
	 */

	public boolean isCaptchaRegisterToMBeanServer() {
		return captchaRegisterToMBeanServer;
	}
	/** Sets the captcha register to m bean server. */

	public void setCaptchaRegisterToMBeanServer(boolean captchaRegisterToMBeanServer) {
		this.captchaRegisterToMBeanServer = captchaRegisterToMBeanServer;
	}
	/** Gets the captcha question parameter name. */

	public String getCaptchaQuestionParameterName() {
		return captchaQuestionParameterName;
	}
	/** Sets the captcha question parameter name. */

	public void setCaptchaQuestionParameterName(String captchaQuestionParameterName) {
		this.captchaQuestionParameterName = captchaQuestionParameterName;
	}
	/** Gets the captcha rendering u r l. */

	public String getCaptchaRenderingURL() {
		return captchaRenderingURL;
	}
	/** Sets the captcha rendering u r l. */

	public void setCaptchaRenderingURL(String captchaRenderingURL) {
		this.captchaRenderingURL = captchaRenderingURL;
	}
	/** Gets the captcha error u r l. */

	public String getCaptchaErrorURL() {
		return captchaErrorURL;
	}
	/** Sets the captcha error u r l. */

	public void setCaptchaErrorURL(String captchaErrorURL) {
		this.captchaErrorURL = captchaErrorURL;
	}
	/** Gets the captcha challenge response parameter name. */

	public String getCaptchaChallengeResponseParameterName() {
		return captchaChallengeResponseParameterName;
	}
	/** Sets the captcha challenge response parameter name. */

	public void setCaptchaChallengeResponseParameterName(String captchaChallengeResponseParameterName) {
		this.captchaChallengeResponseParameterName = captchaChallengeResponseParameterName;
	}
	/** Gets the captcha verification u r ls. */

	public String getCaptchaVerificationURLs() {
		return captchaVerificationURLs;
	}
	/** Sets the captcha verification u r ls. */

	public void setCaptchaVerificationURLs(String captchaVerificationURLs) {
		this.captchaVerificationURLs = captchaVerificationURLs;
	}
	/** Gets the captcha forward error u r ls. */

	public String getCaptchaForwardErrorURLs() {
		return captchaForwardErrorURLs;
	}
	/** Sets the captcha forward error u r ls. */

	public void setCaptchaForwardErrorURLs(String captchaForwardErrorURLs) {
		this.captchaForwardErrorURLs = captchaForwardErrorURLs;
	}
	/** Gets the captcha filter pattern. */

	public String getCaptchaFilterPattern() {
		return captchaFilterPattern;
	}
	/** Sets the captcha filter pattern. */

	public void setCaptchaFilterPattern(String captchaFilterPattern) {
		this.captchaFilterPattern = captchaFilterPattern;
	}
	

}
