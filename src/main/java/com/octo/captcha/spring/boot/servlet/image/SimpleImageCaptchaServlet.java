package com.octo.captcha.spring.boot.servlet.image;


import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import jakarta.servlet.Servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet that generates and serves a JPEG captcha image for the requesting session, and exposes a
 * static helper to validate the captcha response submitted by the user.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class SimpleImageCaptchaServlet extends HttpServlet implements Servlet {

    /** Shared {@link ImageCaptchaService} used to generate and validate challenges. */
    public static ImageCaptchaService service = new DefaultManageableImageCaptchaService();

    /** Default constructor. */
    public SimpleImageCaptchaServlet() {
    }

    /** Generate a captcha image for the requesting session and write it to the response as JPEG. @param httpServletRequest servlet request @param httpServletResponse servlet response @throws ServletException if a servlet error occurs @throws IOException if writing the image fails */
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setDateHeader("Expires", 0L);
        httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        httpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setContentType("image/jpeg");
        BufferedImage bi = service.getImageChallengeForID(httpServletRequest.getSession(true).getId());
        ServletOutputStream out = httpServletResponse.getOutputStream();
        ImageIO.write(bi, "jpg", out);

        try {
            out.flush();
        } finally {
            out.close();
        }

    }

    /** Validate the user's captcha response against the challenge stored for the request's session. @param request servlet request @param userCaptchaResponse the response submitted by the user @return true if the response is correct, false otherwise */
    public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse) {
        if (request.getSession(false) == null) {
            return false;
        } else {
            boolean validated = false;

            try {
                validated = service.validateResponseForID(request.getSession().getId(), userCaptchaResponse);
            } catch (CaptchaServiceException var4) {
                CaptchaServiceException e = var4;
                e.printStackTrace();
            }

            return validated;
        }
    }
}
