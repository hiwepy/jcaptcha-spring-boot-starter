package com.octo.captcha.spring.boot.filter.image;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * Minimal stub implementation of {@link ImageCaptchaService} for unit testing
 * {@link ImageCaptchaFilter} without loading the full jcaptcha engine.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class StubImageCaptchaService implements ImageCaptchaService {

    @Override
    public BufferedImage getImageChallengeForID(String id) throws CaptchaServiceException {
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public BufferedImage getImageChallengeForID(String id, Locale locale) throws CaptchaServiceException {
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public Object getChallengeForID(String id) throws CaptchaServiceException {
        return "challenge";
    }

    @Override
    public Object getChallengeForID(String id, Locale locale) throws CaptchaServiceException {
        return "challenge";
    }

    @Override
    public String getQuestionForID(String id) throws CaptchaServiceException {
        return "question";
    }

    @Override
    public String getQuestionForID(String id, Locale locale) throws CaptchaServiceException {
        return "question";
    }

    @Override
    public Boolean validateResponseForID(String id, Object response) throws CaptchaServiceException {
        return Boolean.TRUE.equals(response) || "correct".equals(response);
    }
}
