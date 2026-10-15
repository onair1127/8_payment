package com.anseong.core.infrastructure.repository.hibernate.user;

import static com.anseong.core.domain.model.user.QUser.user;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.user.ImageUser;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateUserCommandRepository extends HibernateSupport<User> implements UserCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateUserCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public void updatePaymentUid(UserId userId, String customerUid, String merchantUid) {
          jpaQueryFactory
               .update(user)
               .set(user.customerUid, customerUid)
               .set(user.merchantUid, merchantUid)
               .where(user.id.eq(userId.value()))
               .execute();
     }
}
