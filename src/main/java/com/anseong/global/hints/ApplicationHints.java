package com.anseong.global.hints;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.apache.catalina.core.ApplicationFilterRegistration;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.collection.spi.AbstractPersistentCollection;
import org.hibernate.collection.spi.PersistentSet;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.AbstractBaseId;

public class ApplicationHints implements RuntimeHintsRegistrar {

     private final List<Class<?>> reflectionClassTarget = List.of(
          ApplicationFilterRegistration.class
     );

     private final List<Class<? extends Serializable>> abstractClassTarget = List.of(
          AbstractBaseId.class, AbstractBaseEntity.class
     );

     private final List<Class<? extends Serializable>> classTarget = List.of(
          HashSet.class, Number.class, LocalDateTime.class, AbstractPersistentCollection.class, PersistentSet.class
     );

     @Override
     public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

          /**
           * 위 컬렉션에 등록한 애들부터 Reflection, Serialization Hints 등록
           */
          reflectionClassTarget.forEach(clazz -> {
               hints.reflection().registerType(clazz, hint -> hint.withMembers(
                    MemberCategory.ACCESS_DECLARED_FIELDS,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_METHODS
               ));
               System.out.println("💹 특정 클래스 리플렉션 등록 성공 : " + clazz.getSimpleName());
          });

          /**
           * 애플리케이션에 추상클래스와 구현체들을 등록
           */
          abstractClassTarget.forEach(parent -> {
               hints.reflection().registerType(parent, hint -> hint
                    .withJavaSerialization(true)
                    .withMembers(
                         MemberCategory.ACCESS_DECLARED_FIELDS,
                         MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                         MemberCategory.INVOKE_DECLARED_METHODS
                    )
               );

               System.out.println("✅ 부모 추상 클래스 Reflection, Serialization 등록 성공 : " + parent.getSimpleName());

               var provider = new ClassPathScanningCandidateComponentProvider(false);

               provider.addIncludeFilter(new AssignableTypeFilter(parent));
               provider.findCandidateComponents("com.anseong").forEach(bean -> {
                    try {
                         Class<?> raw = Class.forName(bean.getBeanClassName(), false, classLoader);
                         Class<? extends Serializable> child = raw.asSubclass(Serializable.class);

                         hints.reflection().registerType(parent, hint -> hint
                              .withJavaSerialization(true)
                              .withMembers(
                                   MemberCategory.ACCESS_DECLARED_FIELDS,
                                   MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                   MemberCategory.INVOKE_DECLARED_METHODS
                              )
                         );

                         System.out.println("✅ " + parent.getSimpleName() + " 구현체들 Reflection, Serialization Hint 등록 성공 : " + child.getSimpleName());
                    } catch(Exception e) {
                         System.err.println("💥 " + parent.getSimpleName() + " 구현체들 Reflection, Serialization Hint 등록 실패 -> " + bean.getBeanClassName() + " : " + e.getMessage());
                    }
               });
          });

          classTarget.forEach(clazz -> {
               hints.reflection().registerType(clazz, hint -> hint
                    .withJavaSerialization(true)
                    .withMembers(
                         MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                         MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
                    )
               );

               System.out.println("✅ Serialization Hint 등록 성공 : " + clazz.getName());
          });

          /**
           * Q클래스 힌트
           */
          var qClassProvider = new ClassPathScanningCandidateComponentProvider(false);

          qClassProvider.addIncludeFilter(new AssignableTypeFilter(Object.class));
          qClassProvider.findCandidateComponents("com.anseong.core.domain.model").forEach(bean -> {
               try {
                    String className = bean.getBeanClassName();

                    if (className != null && className.substring(className.lastIndexOf(".") + 1).startsWith("Q")) {

                         try {
                              Class<?> raw = Class.forName(className, false, classLoader);
                              Class<? extends Serializable> qClass = raw.asSubclass(Serializable.class);

                              hints.reflection().registerType(qClass, hint -> hint.withMembers(
                                   MemberCategory.ACCESS_DECLARED_FIELDS,
                                   MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                   MemberCategory.INVOKE_DECLARED_METHODS
                              ));

                              System.out.println("✅ QClass의 Reflection, Serialization Hint 등록 성공 : " + bean.getBeanClassName());
                         } catch (Exception e) {
                              System.err.println("💥 QClass의 Reflection, Serialization Hint 등록 실패 -> " + bean.getBeanClassName() + " : " + e.getMessage());
                         }
                    }

               } catch(Exception e) {
                    System.err.println("💥 QClass의 Reflection Hint 등록 실패 -> " + bean.getBeanClassName() + " : " + e.getMessage());
               }
          });

          /**
           * AOT 구현체 힌트
           */
          var aspectProvider = new ClassPathScanningCandidateComponentProvider(false);

          aspectProvider.addIncludeFilter(new AnnotationTypeFilter(Aspect.class));
          aspectProvider.findCandidateComponents("com.anseong.core.infrastructure.aop").forEach(bean -> {
               try {
                    Class<?> aspectClass = Class.forName(bean.getBeanClassName(), false, classLoader);

                    hints.reflection().registerType(aspectClass, hint -> hint.withMembers(
                         MemberCategory.ACCESS_DECLARED_FIELDS,
                         MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                         MemberCategory.INVOKE_DECLARED_METHODS
                    ));

                    System.out.println("✅ Aspect 구현체의 Reflection Hint 등록 성공 : " + aspectClass.getName());

               } catch (Exception e) {
                    System.err.println("💥 Aspect 구현체의 Reflection Hint 등록 실패 -> " + bean.getBeanClassName() + " : " + e.getMessage());
               }
          });
     }
}
