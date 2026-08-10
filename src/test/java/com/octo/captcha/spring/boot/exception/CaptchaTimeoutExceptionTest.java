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
package com.octo.captcha.spring.boot.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CaptchaTimeoutException}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("CaptchaTimeoutException Tests")
class CaptchaTimeoutExceptionTest {

    @Test
    @DisplayName("Default constructor creates instance with default message")
    void testDefaultConstructor() {
        CaptchaTimeoutException instance = new CaptchaTimeoutException();
        assertThat(instance).isNotNull();
        assertThat(instance.getMessage()).isEqualTo("captcha expired");
    }

    @Test
    @DisplayName("Constructor with message creates instance with custom message")
    void testMessageConstructor() {
        CaptchaTimeoutException instance = new CaptchaTimeoutException("custom");
        assertThat(instance).isNotNull();
        assertThat(instance.getMessage()).isEqualTo("custom");
    }
}
