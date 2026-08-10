/*
 * Copyright (c) 2017, hiwepy (https://github.com/easy-4-java).
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
/**
 * 
 */
package com.octo.captcha.spring.boot.exception;

/**
 * Exception thrown when a captcha challenge response is incorrect.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class CaptchaIncorrectException extends Exception {

	/** Create a new exception with the default message. */
	public CaptchaIncorrectException() {
        super("captcha incorrect");
    }

    /** Create a new exception with the given detail message. @param message detail message */
    public CaptchaIncorrectException(String message) {
        super(message);
    }
	
}
