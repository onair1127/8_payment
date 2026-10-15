package com.anseong.core.application.queries.user.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.user.services.UserQueryRepositoryService;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;

@Service
@Transactional(readOnly = true)
public class UserQueryRepositoryServiceImpl implements UserQueryRepositoryService {

     private final UserQueryRepository userQueryRepository;

     public UserQueryRepositoryServiceImpl(UserQueryRepository userQueryRepository) {
          this.userQueryRepository = userQueryRepository;
     }

     @Override
     public String findMerchantUidById(UserId userId) {
          return userQueryRepository.findMerchantUidById(userId);
     }

     @Override
     public String findCustomerUidById(UserId userId) {
          return userQueryRepository.findCustomerUidById(userId);
     }

     @Override
     public String findUsernameByPhone(String phone) {
          return userQueryRepository.findUsernameByPhone(phone);
     }
}
