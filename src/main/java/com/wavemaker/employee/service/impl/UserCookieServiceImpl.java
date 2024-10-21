package com.wavemaker.employee.service.impl;


import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.repository.UserCookieRepository;
import com.wavemaker.employee.repository.impl.UserCookieRepositoryImpl;
import com.wavemaker.employee.service.UserCookieService;
import com.wavemaker.employee.service.UserEntityService;
import org.springframework.stereotype.Service;

@Service
public class UserCookieServiceImpl implements UserCookieService {

    private UserEntityService userEntityService;

    private UserCookieRepository userCookieRepository;


    public UserCookieServiceImpl() {
        this.userEntityService = new UserEntityServiceImpl();
        this.userCookieRepository = new UserCookieRepositoryImpl();
    }


    @Override
    public boolean addCookie(String cookieValue, int userId) throws ServerUnavilableException {
        return userCookieRepository.addCookie(cookieValue, userId);
    }

    @Override
    public UserEntity getUserEntityByCookieValue(String cookieValue) throws ServerUnavilableException {
        int userId = userCookieRepository.getUserIdByCookieValue(cookieValue);
        if (userId != -1) {
            return userEntityService.getUserEntityById(userId);
        }
        return null;
    }

    @Override
    public boolean deleteUserCookie(String cookieValue) throws ServerUnavilableException {
        return userCookieRepository.deleteUserCookie(cookieValue);
    }
}
