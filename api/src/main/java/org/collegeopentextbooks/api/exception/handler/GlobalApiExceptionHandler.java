package org.collegeopentextbooks.api.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.exception.ResponseException;
import org.collegeopentextbooks.api.exception.RestApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Logs all exceptions emitted from request controllers. If the exception class is annotated with
 * @{@link ResponseException}, the response's HTTP status code and reason are changed to match the annotation's
 * status and <code>reason</code>.
 *
 * If the thrown exception does NOT have the @{@link ResponseException} annotation, the response is changed to
 * HTTP 500 Internal Server Error with a reason matching the exception's message.
 *
 * @author steve.perkins
 */
@ControllerAdvice(basePackages = "org.collegeopentextbooks.api.controller")
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiException> handleError(HttpServletRequest request, Exception e)
    {
    	logger.error("API EXCEPTION THROWN", e);
    	Class<? extends Exception> errorClass = e.getClass();
        if(!errorClass.isAnnotationPresent(ResponseException.class)) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        		.body(new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
        
        ResponseException annotation = errorClass.getAnnotation(ResponseException.class);
        return ResponseEntity.status(annotation.code())
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(new RestApiException(annotation.code().value(), (StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : annotation.reason())));
    }
    
}