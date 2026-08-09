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
package com.octo.captcha.spring.boot.filter.image;

import com.octo.captcha.service.image.ImageCaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ImageCaptchaFilter}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("ImageCaptchaFilter Tests")
class ImageCaptchaFilterTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        ImageCaptchaFilter instance = new ImageCaptchaFilter();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("Constants have expected values")
    void testConstants() {
        assertThat(ImageCaptchaFilter.JMX_REGISTERING_NAME).isEqualTo("com.octo.captcha.module.servlet:object=ImageCaptchaFilter");
        assertThat(ImageCaptchaFilter.CSV_DELIMITER).isEqualTo(";");
        assertThat(ImageCaptchaFilter.CAPTCHA_ERROR_URL_PARAMETER).isEqualTo("CaptchaErrorURL");
        assertThat(ImageCaptchaFilter.CAPTCHA_RENDERING_URL_PARAMETER).isEqualTo("CaptchaRenderingURL");
        assertThat(ImageCaptchaFilter.CAPTCHA_VERIFICATION_URLS_PARAMETER).isEqualTo("CaptchaVerificationURLs");
        assertThat(ImageCaptchaFilter.CAPTCHA_FAIL_URLS_PARAMETER).isEqualTo("CaptchaFailURLs");
        assertThat(ImageCaptchaFilter.CAPTCHA_QUESTION_NAME_PARAMETER).isEqualTo("CaptchaQuestionParameterName");
        assertThat(ImageCaptchaFilter.CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER).isEqualTo("CaptchaChallengeResponseParameterName");
        assertThat(ImageCaptchaFilter.CAPTCHA_SERVICE_CLASS_PARAMETER).isEqualTo("ImageCaptchaServiceClass");
        assertThat(ImageCaptchaFilter.CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER).isEqualTo("RegisterToMBeanServer");
    }

    @Test
    @DisplayName("init() reads parameters and instantiates service")
    void testInit() throws ServletException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/verify1;/verify2");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/fail1;/fail2");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");

        filter.init(config);

        assertThat(ImageCaptchaFilter.getCaptchaRenderingURL()).isEqualTo("/render");
        assertThat(ImageCaptchaFilter.getCaptchaQuestionParameterName()).isEqualTo("question");
        assertThat(ImageCaptchaFilter.getCaptchaChallengeResponseParameterName()).isEqualTo("response");
    }

    @Test
    @DisplayName("init() throws when verification and fail URL counts mismatch")
    void testInitUrlMismatch() {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/v1;/v2");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/f1");
        when(config.getFilterName()).thenReturn("testFilter");

        assertThatThrownBy(() -> filter.init(config))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("not consistant");
    }

    @Test
    @DisplayName("doFilter() renders captcha when path matches rendering URL")
    void testDoFilterRender() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        // Set up the filter via init
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/captcha/render");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            private java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            @Override
            public void write(int b) { baos.write(b); }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setWriteListener(jakarta.servlet.WriteListener listener) {}
        });

        filter.doFilter(request, response, chain);

        verify(response).setContentType("image/jpeg");
        verify(response).setHeader(eq("Cache-Control"), anyString());
    }

    @Test
    @DisplayName("doFilter() verifies captcha when path matches verification URL")
    void testDoFilterVerify() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/verify");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/fail");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/verify");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getParameter("response")).thenReturn("correct");
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        // Correct response should forward through chain
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter() redirects to error when captcha response is null")
    void testDoFilterVerifyNullResponse() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/verify");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/fail");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/verify");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getParameter("response")).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/fail");
    }

    @Test
    @DisplayName("doFilter() redirects to error when captcha response is wrong")
    void testDoFilterVerifyWrongResponse() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/verify");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/fail");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/verify");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getParameter("response")).thenReturn("wrong");
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/fail");
    }

    @Test
    @DisplayName("doFilter() adds question to request for non-captcha paths")
    void testDoFilterPassthrough() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("/verify");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("/fail");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/other");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        filter.doFilter(request, response, chain);

        verify(request).setAttribute(eq("question"), any());
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("destroy() completes without error")
    void testDestroy() throws ServletException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        // Should not throw
        filter.destroy();
    }

    @Test
    @DisplayName("doFilter() redirects to error when captcha service throws")
    void testDoFilterRenderError() throws Exception {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        // Replace service with one that throws
        ImageCaptchaService throwingService = mock(ImageCaptchaService.class);
        when(throwingService.getImageChallengeForID(anyString(), any()))
                .thenThrow(new com.octo.captcha.service.CaptchaServiceException("test error"));
        java.lang.reflect.Field serviceField = ImageCaptchaFilter.class.getDeclaredField("captchaService");
        serviceField.setAccessible(true);
        serviceField.set(filter, throwingService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/captcha/render");
        when(request.getQueryString()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        filter.doFilter(request, response, chain);

        verify(response).sendError(404);
    }

    @Test
    @DisplayName("doFilter() handles query string in path")
    void testDoFilterWithQueryString() throws ServletException, IOException {
        ImageCaptchaFilter filter = new ImageCaptchaFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("CaptchaRenderingURL")).thenReturn("/captcha/render");
        when(config.getInitParameter("CaptchaErrorURL")).thenReturn("/error");
        when(config.getInitParameter("CaptchaVerificationURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaFailURLs")).thenReturn("");
        when(config.getInitParameter("CaptchaQuestionParameterName")).thenReturn("question");
        when(config.getInitParameter("CaptchaChallengeResponseParameterName")).thenReturn("response");
        when(config.getInitParameter("ImageCaptchaServiceClass")).thenReturn(StubImageCaptchaService.class.getName());
        when(config.getInitParameter("RegisterToMBeanServer")).thenReturn("false");
        when(config.getFilterName()).thenReturn("testFilter");
        filter.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getServletPath()).thenReturn("/other");
        when(request.getQueryString()).thenReturn("foo=bar");
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
