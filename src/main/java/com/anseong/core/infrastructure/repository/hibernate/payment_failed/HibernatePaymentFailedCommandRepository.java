package com.anseong.core.infrastructure.repository.hibernate.payment_failed;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.payment_failed.PaymentFailed;
import com.anseong.core.domain.model.payment_failed.repository.PaymentFailedCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;

import jakarta.persistence.EntityManager;

@Repository
class HibernatePaymentFailedCommandRepository extends HibernateSupport<PaymentFailed> implements PaymentFailedCommandRepository {

     HibernatePaymentFailedCommandRepository(EntityManager entityManager) {
          super(entityManager);
     }
}
