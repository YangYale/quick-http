package com.ipet.http.spring;

import com.ipet.http.annotation.HttpComponent;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 14:06
 */
public class ClassPathHttpComponentScanner extends ClassPathBeanDefinitionScanner {
    public ClassPathHttpComponentScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public ClassPathHttpComponentScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathHttpComponentScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public ClassPathHttpComponentScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if(beanDefinitions.isEmpty()){
            logger.warn("No Http Components was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }else{
            this.processBeanDefinitionHolders(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitionHolders(Set<BeanDefinitionHolder> beanDefinitionHolders){
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinition : beanDefinitionHolders) {
            definition = (GenericBeanDefinition)beanDefinition.getBeanDefinition();
            //注入interface时确保注入的是代理
            definition.getPropertyValues().add("interfaceClassName",definition.getBeanClassName());
            // 注入代理对象
            definition.setBeanClass(HttpRequestFactoryBean.class);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) || beanDefinition.getMetadata().hasAnnotation(HttpComponent.class.getName());
    }

    @Override
    protected void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(HttpComponent.class));
    }
}
