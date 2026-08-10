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
package com.octo.captcha.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JCaptchaProperties}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JCaptchaProperties Tests")
class JCaptchaPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance with expected defaults")
    void testDefaultInstance() {
        JCaptchaProperties props = new JCaptchaProperties();
        assertThat(props).isNotNull();
        assertThat(props.getType()).isEqualTo(JCaptchaProperties.JCaptchaType.SERVLET);
        assertThat(props.getCaptchaTimeout()).isEqualTo(JCaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT);
        assertThat(props.getCaptchaServletPattern()).isEqualTo("/jcaptcha.jpg");
        assertThat(props.getCaptchaFilterPattern()).isEqualTo("/jcaptcha");
        assertThat(props.isCaptchaRegisterToMBeanServer()).isFalse();
        assertThat(props.getCaptchaVerificationURLs()).isEmpty();
        assertThat(props.getCaptchaForwardErrorURLs()).isEmpty();
    }

    @Test
    @DisplayName("Public constant 'PREFIX' has expected value")
    void testPREFIXConstant() {
        assertThat(JCaptchaProperties.PREFIX).isEqualTo("jcaptcha");
    }

    @Test
    @DisplayName("Public constant 'DEFAULT_CAPTCHA_TIMEOUT' has expected value")
    void testDefaultCaptchaTimeout() {
        assertThat(JCaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT).isEqualTo(60_000L);
    }

    @Test
    @DisplayName("Field 'type' can be set and read")
    void testTypeField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setType(JCaptchaProperties.JCaptchaType.FILTER);
        assertThat(props.getType()).isEqualTo(JCaptchaProperties.JCaptchaType.FILTER);
    }

    @Test
    @DisplayName("Field 'captchaStoreKey' can be set and read")
    void testCaptchaStoreKeyField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaStoreKey("myKey");
        assertThat(props.getCaptchaStoreKey()).isEqualTo("myKey");
    }

    @Test
    @DisplayName("Field 'captchaDateStoreKey' can be set and read")
    void testCaptchaDateStoreKeyField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaDateStoreKey("myDateKey");
        assertThat(props.getCaptchaDateStoreKey()).isEqualTo("myDateKey");
    }

    @Test
    @DisplayName("Field 'captchaTimeout' can be set and read")
    void testCaptchaTimeoutField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaTimeout(42L);
        assertThat(props.getCaptchaTimeout()).isEqualTo(42L);
    }

    @Test
    @DisplayName("Field 'captchaServletPattern' can be set and read")
    void testCaptchaServletPatternField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaServletPattern("/custom.jpg");
        assertThat(props.getCaptchaServletPattern()).isEqualTo("/custom.jpg");
    }

    @Test
    @DisplayName("Field 'captchaFilterPattern' can be set and read")
    void testCaptchaFilterPatternField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaFilterPattern("/custom");
        assertThat(props.getCaptchaFilterPattern()).isEqualTo("/custom");
    }

    @Test
    @DisplayName("Field 'captchaRegisterToMBeanServer' can be set and read")
    void testCaptchaRegisterToMBeanServerField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaRegisterToMBeanServer(true);
        assertThat(props.isCaptchaRegisterToMBeanServer()).isTrue();
    }

    @Test
    @DisplayName("Field 'captchaQuestionParameterName' can be set and read")
    void testCaptchaQuestionParameterNameField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaQuestionParameterName("question");
        assertThat(props.getCaptchaQuestionParameterName()).isEqualTo("question");
    }

    @Test
    @DisplayName("Field 'captchaRenderingURL' can be set and read")
    void testCaptchaRenderingURLField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaRenderingURL("/render");
        assertThat(props.getCaptchaRenderingURL()).isEqualTo("/render");
    }

    @Test
    @DisplayName("Field 'captchaErrorURL' can be set and read")
    void testCaptchaErrorURLField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaErrorURL("/error");
        assertThat(props.getCaptchaErrorURL()).isEqualTo("/error");
    }

    @Test
    @DisplayName("Field 'captchaChallengeResponseParameterName' can be set and read")
    void testCaptchaChallengeResponseParameterNameField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaChallengeResponseParameterName("response");
        assertThat(props.getCaptchaChallengeResponseParameterName()).isEqualTo("response");
    }

    @Test
    @DisplayName("Field 'captchaVerificationURLs' can be set and read")
    void testCaptchaVerificationURLsField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaVerificationURLs("/verify");
        assertThat(props.getCaptchaVerificationURLs()).isEqualTo("/verify");
    }

    @Test
    @DisplayName("Field 'captchaForwardErrorURLs' can be set and read")
    void testCaptchaForwardErrorURLsField() {
        JCaptchaProperties props = new JCaptchaProperties();
        props.setCaptchaForwardErrorURLs("/fwd");
        assertThat(props.getCaptchaForwardErrorURLs()).isEqualTo("/fwd");
    }
}
