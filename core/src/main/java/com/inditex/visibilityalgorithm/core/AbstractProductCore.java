package com.inditex.visibilityalgorithm.core;

import com.inditex.visibilityalgorithm.facade.Facade;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract class AbstractProductCore implements ProductCore {

  @Autowired
  private Facade<ProductCore> productFacade;

  @PostConstruct
  void init() {
    Integer version = Optional.ofNullable(this.getClass().getAnnotation(Implementation.class))
        .map(Implementation::version)
        .orElseThrow(() -> new FatalBeanException("AbstractProductCore implementation should be annotated with @Implementation"));

    productFacade.register(version, this);

  }

}
