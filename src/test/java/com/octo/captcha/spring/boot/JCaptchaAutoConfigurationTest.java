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

import com.octo.captcha.service.image.ImageCaptchaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JCaptchaAutoConfiguration}.
 *
 * <p>Verifies the auto-configuration activates under the expected conditions
 * and exposes its declared beans.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("JCaptchaAutoConfiguration Tests")
class JCaptchaAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(JCaptchaAutoConfiguration.class);

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        JCaptchaAutoConfiguration configuration = new JCaptchaAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration loads captchaService and servlet bean when type=servlet")
    void testLoadsWhenEnabledPropertySet() {
        runner.withPropertyValues("jcaptcha.type=servlet")
                .run(context -> {
                    assertThat(context).hasSingleBean(ImageCaptchaService.class);
                    assertThat(context).hasBean("captchaService");
                    assertThat(context).hasSingleBean(ServletRegistrationBean.class);
                    assertThat(context).doesNotHaveBean(FilterRegistrationBean.class);
                });
    }

    @Test
    @DisplayName("Auto-configuration loads captchaService but no servlet/filter bean when property is absent")
    void testNotLoadedWhenPropertyAbsent() {
        runner.run(context -> {
                    assertThat(context).hasSingleBean(ImageCaptchaService.class);
                    assertThat(context).hasBean("captchaService");
                    assertThat(context).doesNotHaveBean(ServletRegistrationBean.class);
                    assertThat(context).doesNotHaveBean(FilterRegistrationBean.class);
                });
    }

    @Test
    @DisplayName("Auto-configuration loads filter bean when type=filter")
    void testLoadsFilterWhenTypeFilter() {
        runner.withPropertyValues("jcaptcha.type=filter")
                .run(context -> {
                    assertThat(context).hasSingleBean(ImageCaptchaService.class);
                    assertThat(context).hasSingleBean(FilterRegistrationBean.class);
                    assertThat(context).doesNotHaveBean(ServletRegistrationBean.class);
                });
    }

    @Test
    @DisplayName("JCaptchaType enum resolves case-insensitively")
    void testJCaptchaTypeEnum() {
        assertThat(JCaptchaProperties.JCaptchaType.valueOfIgnoreCase("filter"))
                .isEqualTo(JCaptchaProperties.JCaptchaType.FILTER);
        assertThat(JCaptchaProperties.JCaptchaType.valueOfIgnoreCase("SERVLET"))
                .isEqualTo(JCaptchaProperties.JCaptchaType.SERVLET);
    }

    @Test
    @DisplayName("JCaptchaType enum get() returns the string value")
    void testJCaptchaTypeGet() {
        assertThat(JCaptchaProperties.JCaptchaType.FILTER.get()).isEqualTo("filter");
        assertThat(JCaptchaProperties.JCaptchaType.SERVLET.get()).isEqualTo("servlet");
    }

    @Test
    @DisplayName("JCaptchaType enum equals methods work correctly")
    void testJCaptchaTypeEquals() {
        JCaptchaProperties.JCaptchaType filter = JCaptchaProperties.JCaptchaType.FILTER;
        assertThat(filter.equals(JCaptchaProperties.JCaptchaType.FILTER)).isTrue();
        assertThat(filter.equals(JCaptchaProperties.JCaptchaType.SERVLET)).isFalse();
        assertThat(filter.equals("filter")).isTrue();
        assertThat(filter.equals("FILTER")).isTrue();
        assertThat(filter.equals("servlet")).isFalse();
    }

    @Test
    @DisplayName("JCaptchaType valueOfIgnoreCase throws for unknown key")
    void testJCaptchaTypeValueOfIgnoreCaseThrows() {
        org.junit.jupiter.api.Assertions.assertThrows(
                java.util.NoSuchElementException.class,
                () -> JCaptchaProperties.JCaptchaType.valueOfIgnoreCase("unknown")
        );
    }
}
