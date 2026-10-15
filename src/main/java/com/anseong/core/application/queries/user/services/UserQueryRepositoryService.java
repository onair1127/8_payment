package com.anseong.core.application.queries.user.services;

import com.anseong.core.domain.model.user.UserId;

public interface UserQueryRepositoryService {
     String findMerchantUidById(UserId userId);

     String findCustomerUidById(UserId userId);

     String findUsernameByPhone(String phone);
}
