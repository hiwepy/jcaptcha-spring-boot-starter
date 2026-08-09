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
package com.octo.captcha.spring.boot.filter;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FilterConfigUtils}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("FilterConfigUtils Tests")
class FilterConfigUtilsTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        FilterConfigUtils instance = new FilterConfigUtils();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("getStringInitParameter returns value when present")
    void testGetStringInitParameterValue() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn("value");
        assertThat(FilterConfigUtils.getStringInitParameter(config, "param", false)).isEqualTo("value");
    }

    @Test
    @DisplayName("getStringInitParameter returns null when optional and absent")
    void testGetStringInitParameterOptionalAbsent() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn(null);
        assertThat(FilterConfigUtils.getStringInitParameter(config, "param", false)).isNull();
    }

    @Test
    @DisplayName("getStringInitParameter throws when mandatory and absent")
    void testGetStringInitParameterMandatoryAbsent() {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn(null);
        when(config.getFilterName()).thenReturn("testFilter");
        assertThatThrownBy(() -> FilterConfigUtils.getStringInitParameter(config, "param", true))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("param");
    }

    @Test
    @DisplayName("getIntegerInitParameter returns value when present and in range")
    void testGetIntegerInitParameterValue() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn("5");
        assertThat(FilterConfigUtils.getIntegerInitParameter(config, "param", false, 0, 10)).isEqualTo(5);
    }

    @Test
    @DisplayName("getIntegerInitParameter throws when mandatory and absent")
    void testGetIntegerInitParameterMandatoryAbsent() {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn(null);
        when(config.getFilterName()).thenReturn("testFilter");
        assertThatThrownBy(() -> FilterConfigUtils.getIntegerInitParameter(config, "param", true, 0, 10))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("param");
    }

    @Test
    @DisplayName("getIntegerInitParameter throws when not a number")
    void testGetIntegerInitParameterNotNumber() {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn("abc");
        when(config.getFilterName()).thenReturn("testFilter");
        assertThatThrownBy(() -> FilterConfigUtils.getIntegerInitParameter(config, "param", false, 0, 10))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("integer");
    }

    @Test
    @DisplayName("getIntegerInitParameter throws when out of range")
    void testGetIntegerInitParameterOutOfRange() {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn("99");
        when(config.getFilterName()).thenReturn("testFilter");
        assertThatThrownBy(() -> FilterConfigUtils.getIntegerInitParameter(config, "param", false, 0, 10))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining(">=");
    }

    @Test
    @DisplayName("getBooleanInitParameter returns true when value is 'true'")
    void testGetBooleanInitParameterTrue() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn("true");
        assertThat(FilterConfigUtils.getBooleanInitParameter(config, "param", false)).isTrue();
    }

    @Test
    @DisplayName("getBooleanInitParameter returns false when optional and absent")
    void testGetBooleanInitParameterOptionalAbsent() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn(null);
        assertThat(FilterConfigUtils.getBooleanInitParameter(config, "param", false)).isFalse();
    }

    @Test
    @DisplayName("getBooleanInitParameter throws when mandatory and absent")
    void testGetBooleanInitParameterMandatoryAbsent() {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("param")).thenReturn(null);
        when(config.getFilterName()).thenReturn("testFilter");
        assertThatThrownBy(() -> FilterConfigUtils.getBooleanInitParameter(config, "param", true))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("param");
    }
}
