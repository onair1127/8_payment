package com.anseong.core.infrastructure.repository.hibernate;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.hibernate.Session;

import com.anseong.core.domain.model.AbstractBaseEntity;

public abstract class HibernateSupport<T extends AbstractBaseEntity> {

     EntityManager entityManager;
     Class<T> entityClass;

     public HibernateSupport(EntityManager entityManager) {
          this.entityManager = entityManager;
     }

     public Session getSession() {
          return entityManager.unwrap(Session.class);
     }

     public T find(long id) {
          return entityManager.find(entityClass, id);
     }

     public T getReference(long id) {
          return entityManager.getReference(entityClass, id);
     }

     public <R> R getReference(Class<R> anotherEntityClass, long id) {
          return entityManager.getReference(anotherEntityClass, id);
     }

     public void save(T object) {
          entityManager.persist(object);
          // entityManager.flush();
     }

     public void save(List<T> objects) {
          for (T object : objects) {
               entityManager.persist(object);
          }
          // entityManager.flush();
     }

     public void remove(T entity) {
          if (!entityManager.contains(entity))
               entityManager.merge(entity);

          entityManager.remove(entity);
          // entityManager.flush();
     }

     public void remove(List<T> entities) {
          for (T entity : entities) {
               if (!entityManager.contains(entity))
                    entityManager.merge(entity);

               entityManager.remove(entity);
          }
          // entityManager.flush();
     }
}
