package com.wavemaker.employee.service.impl;


import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.repository.UserEntityRepository;
import com.wavemaker.employee.repository.impl.UserEntityRepositoryImpl;
import com.wavemaker.employee.service.UserEntityService;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private UserEntityRepository userEntityRepository;

    public UserEntityServiceImpl() {
        userEntityRepository = new UserEntityRepositoryImpl();
    }

    @Override
    public UserEntity authenticateUser(UserEntity userEntity) throws ServerUnavilableException {
        return userEntityRepository.authenticateUser(userEntity);
    }

    @Override
    public UserEntity getUserEntityById(int userId) throws ServerUnavilableException {
        return userEntityRepository.getUserEntityById(userId);
    }

    @Override
    public UserEntity addUserEntity(UserEntity userEntity) throws ServerUnavilableException {
        return userEntityRepository.addUserEntity(userEntity);
    }


}
