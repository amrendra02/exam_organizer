package com.exam_organizer.controller;
import jakarta.servlet.RequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import java.util.Map;

@Controller
public class CustomErrorController {

    private Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/failed")
    public String handleError(WebRequest webRequest, Model model) {
        // Fetch error attributes
        Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)); // Include stack trace

        // Retrieve status code and error message
        Integer statusCode = (Integer) errorAttributesMap.get(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = (String) errorAttributesMap.get(RequestDispatcher.ERROR_MESSAGE);

        // Default status code and message if error message is null
        if (statusCode == null) {
            statusCode = 500; // Default to internal server error if status code is null
            errorMessage = "Internal Server Error";
        }

        // Add error attributes to the model
        model.addAttribute("status_code", statusCode);
        model.addAttribute("message", errorMessage);

        log.info("Error Model: {}", model);
        return "error"; // Forward all error requests to the global error page
    }
}