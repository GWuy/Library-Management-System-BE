package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.repository.UserRepository;
import com.lms.swd392.lmsbe.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public User register(User user) {


        return null;
    }
}
