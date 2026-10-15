package com.anseong.core.domain.model.user.repository;

import com.anseong.core.domain.model.user.UserId;

public interface UserQueryRepository {
     String findMerchantUidById(UserId userId);

     String findCustomerUidById(UserId userId);

     String findUsernameByPhone(String phone);
}
