package com.anseong.core.infrastructure.repository.hibernate.user;

import static com.anseong.core.domain.model.user.QUser.user;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;
import com.anseong.core.domain.model.user.response.UserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateUserQueryRepository implements UserQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateUserQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public String findMerchantUidById(UserId userId) {
          return jpaQueryFactory
               .select(user.merchantUid)
               .from(user)
               .where(user.id.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public String findCustomerUidById(UserId userId) {
          return jpaQueryFactory
               .select(user.customerUid)
               .from(user)
               .where(user.id.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public String findUsernameByPhone(String phone) {
          return jpaQueryFactory
               .select(user.username)
               .from(user)
               .where(user.phone.eq(phone))
               .fetchFirst();
     }
}
