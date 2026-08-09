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
package com.octo.captcha.spring.boot.servlet.image;

import com.octo.captcha.service.image.ImageCaptchaService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import com.octo.captcha.service.CaptchaServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SimpleImageCaptchaServlet}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("SimpleImageCaptchaServlet Tests")
class SimpleImageCaptchaServletTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        SimpleImageCaptchaServlet servlet = new SimpleImageCaptchaServlet();
        assertThat(servlet).isNotNull();
    }

    @Test
    @DisplayName("validateResponse returns false when session is null")
    void testValidateResponseNoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);
        assertThat(SimpleImageCaptchaServlet.validateResponse(request, "anything")).isFalse();
    }

    @Test
    @DisplayName("validateResponse returns true for correct response")
    void testValidateResponseCorrect() {
        ImageCaptchaService mockService = mock(ImageCaptchaService.class);
        when(mockService.validateResponseForID("test-session", "correct")).thenReturn(true);
        SimpleImageCaptchaServlet.service = mockService;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");

        assertThat(SimpleImageCaptchaServlet.validateResponse(request, "correct")).isTrue();
    }

    @Test
    @DisplayName("validateResponse returns false for incorrect response")
    void testValidateResponseIncorrect() {
        ImageCaptchaService mockService = mock(ImageCaptchaService.class);
        when(mockService.validateResponseForID("test-session", "wrong")).thenReturn(false);
        SimpleImageCaptchaServlet.service = mockService;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");

        assertThat(SimpleImageCaptchaServlet.validateResponse(request, "wrong")).isFalse();
    }

    @Test
    @DisplayName("validateResponse returns false when service throws CaptchaServiceException")
    void testValidateResponseException() {
        ImageCaptchaService mockService = mock(ImageCaptchaService.class);
        when(mockService.validateResponseForID("test-session", "anything"))
                .thenThrow(new CaptchaServiceException("test error"));
        SimpleImageCaptchaServlet.service = mockService;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("test-session");

        assertThat(SimpleImageCaptchaServlet.validateResponse(request, "anything")).isFalse();
    }

    @Test
    @DisplayName("doGet writes JPEG captcha image to response")
    void testDoGet() throws Exception {
        // Set a mock service so doGet can use it
        ImageCaptchaService mockService = mock(ImageCaptchaService.class);
        when(mockService.getImageChallengeForID("test-session"))
                .thenReturn(new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_RGB));
        SimpleImageCaptchaServlet.service = mockService;

        SimpleImageCaptchaServlet servlet = new SimpleImageCaptchaServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn("test-session");

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) { baos.write(b); }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setWriteListener(WriteListener listener) {}
        };
        when(response.getOutputStream()).thenReturn(sos);

        servlet.doGet(request, response);

        verify(response).setContentType("image/jpeg");
        verify(response).setHeader("Pragma", "no-cache");
    }
}
