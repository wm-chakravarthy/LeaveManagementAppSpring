package com.wavemaker.employee.login;

import com.wavemaker.employee.exception.ServerUnavailableException;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.pojo.dto.EmployeeVO;
import com.wavemaker.employee.service.EmployeeService;
import com.wavemaker.employee.service.UserCookieService;
import com.wavemaker.employee.service.UserEntityService;
import com.wavemaker.employee.util.CookieHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private UserCookieService userCookieService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public String login(@RequestBody UserEntity userEntity,
                        HttpServletRequest request, HttpServletResponse response) throws ServerUnavailableException, IOException {

        logger.info("User details : {}", userEntity);
        UserEntity authenticatedUser = userEntityService.authenticateUser(userEntity);
        if (authenticatedUser != null) {
            String cookieValue = UUID.randomUUID().toString(); //generating random cookie
            String cookieName = "my_auth_cookie";  //set default cookie value
            Cookie cookie = new Cookie(cookieName, cookieValue); //creating a cookie with name,value;

            userCookieService.addCookie(cookieValue, authenticatedUser.getEmpId());

            logger.info("user cookie added successfully email : {} and cookie : {}", authenticatedUser.getEmail(), cookieValue);
            response.addCookie(cookie); //added cookie to response

            logger.info("User Cookie added Successfully to browser : {}", cookie);

            return "Login Success";
        }
        return "Login Failed";
    }

    @GetMapping
    public EmployeeVO getLoggedInEmployee(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServerUnavailableException {
        EmployeeVO employeeVO = null;
        String cookieValue = CookieHandler.getCookieValueByCookieName("my_auth_cookie", httpServletRequest);
        UserEntity userEntity = null;
        userEntity = userCookieService.getUserEntityByCookieValue(cookieValue);
        logger.info("User found in cookie : {}", userEntity);
        employeeVO = employeeService.getEmployeeById(userEntity.getEmpId());
        logger.info("Employee details fetched: {}", employeeVO);
        return employeeVO;
    }

}
