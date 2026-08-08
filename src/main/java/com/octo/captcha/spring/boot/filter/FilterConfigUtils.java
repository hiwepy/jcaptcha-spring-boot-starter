package com.octo.captcha.spring.boot.filter;


import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;

/**
 * Utility for reading typed init parameters (string, integer, boolean) from a servlet {@link FilterConfig},
 * throwing {@link ServletException} when mandatory parameters are missing or invalid.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class FilterConfigUtils {
    /** Default constructor. */
    public FilterConfigUtils() {
    }

    /** Read a string init parameter, throwing {@link ServletException} if mandatory and missing. @param theFilterConfig filter config @param theInitParameterName parameter name @param isMandatory whether the parameter is mandatory @return the parameter value @throws ServletException if a mandatory parameter is missing */
    public static String getStringInitParameter(FilterConfig theFilterConfig, String theInitParameterName, boolean isMandatory) throws ServletException {
        String returnedValue = theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && returnedValue == null) {
            throw new ServletException(theInitParameterName + " parameter must be declared for " + theFilterConfig.getFilterName() + " in web.xml");
        } else {
            return returnedValue;
        }
    }

    /** Read an integer init parameter within the given bounds, throwing {@link ServletException} if mandatory, unparseable or out of range. @param theFilterConfig filter config @param theInitParameterName parameter name @param isMandatory whether the parameter is mandatory @param theMinValue minimum allowed value @param theMaxValue maximum allowed value @return the parameter value @throws ServletException if the parameter is missing, not an integer or out of range */
    public static Integer getIntegerInitParameter(FilterConfig theFilterConfig, String theInitParameterName, boolean isMandatory, int theMinValue, int theMaxValue) throws ServletException {
        Integer returnedValue = null;
        String returnedValueAsString = theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && returnedValueAsString == null) {
            throw new ServletException(theInitParameterName + " parameter must be declared for " + theFilterConfig.getFilterName() + " in web.xml");
        } else {
            try {
                returnedValue = new Integer(returnedValueAsString);
            } catch (NumberFormatException var8) {
                throw new ServletException(theInitParameterName + " parameter must be an integer value " + theFilterConfig.getFilterName() + " in web.xml");
            }

            if (returnedValue >= theMinValue && returnedValue <= theMaxValue) {
                return returnedValue;
            } else {
                throw new ServletException(theInitParameterName + " parameter for " + theFilterConfig.getFilterName() + " in web.xml must be >= " + theMinValue + " and <= " + theMaxValue);
            }
        }
    }

    /** Read a boolean init parameter, throwing {@link ServletException} if mandatory and missing. @param theFilterConfig filter config @param theInitParameterName parameter name @param isMandatory whether the parameter is mandatory @return the parameter value, or false if unset @throws ServletException if a mandatory parameter is missing */
    public static boolean getBooleanInitParameter(FilterConfig theFilterConfig, String theInitParameterName, boolean isMandatory) throws ServletException {
        String returnedValueAsString = theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && returnedValueAsString == null) {
            throw new ServletException(theInitParameterName + " parameter must be declared for " + theFilterConfig.getFilterName() + " in web.xml");
        } else {
            boolean returnedValue = false;
            if (returnedValueAsString != null) {
                returnedValue = new Boolean(returnedValueAsString);
            }

            return returnedValue;
        }
    }
}
