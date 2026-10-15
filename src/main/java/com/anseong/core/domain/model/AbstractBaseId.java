package com.anseong.core.domain.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public abstract class AbstractBaseId implements Serializable {

     private static final long serialVersionUID = -6501363577336431593L;

     private long id;

     @JsonCreator
     public AbstractBaseId(long id) {
          this.id = id;
     }

     @JsonValue
     public Long value() {
          return id;
     }

     public boolean isValid() {
          return id >= 0;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof AbstractBaseId)) {
               return false;
          }
          AbstractBaseId abstractBaseId = (AbstractBaseId) o;
          return id == abstractBaseId.id;
     }

     @Override
     public int hashCode() {
          return Objects.hashCode(id);
     }
}
