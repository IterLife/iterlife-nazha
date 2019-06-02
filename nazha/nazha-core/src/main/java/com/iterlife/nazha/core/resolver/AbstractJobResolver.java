/**
 * @project nazha-core
 * @file com.iterlife.nazha.core.resolver.AbstractJobResolver.java
 * @version 1.0.0
 * Copyright 2019 - 2019 for Lu Jie
 * https://www.iterlife.com
 **/
package com.iterlife.nazha.core.resolver;

import com.iterlife.nazha.core.entity.JobEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Lu Jie
 * @desc
 * @date 2019 2019年2月13日 下午2:09:57
 * @tags
 */
public abstract class AbstractJobResolver implements EnvironmentCapable, ResourceLoaderAware {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    protected final Log logger = LogFactory.getLog(getClass());

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

    private Environment environment;

    private ResourcePatternResolver resourcePatternResolver;

    private MetadataReaderFactory metadataReaderFactory;

    public AbstractJobResolver(Environment environment) {
        setEnvironment(environment);
        setResourceLoader(null);
    }

    /**
     * 解析 Job 注解生成具体的 Job 实例
     */
    public void resolver() {
    }

    /**
     * 解析 Job 注解生成具体的 Job 实例
     *
     * @return JobEntity Job 实例
     */
    public abstract JobEntity resolver2Entity();

    /**
     * Add an include type filter to the <i>end</i> of the inclusion list.
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * Add an exclude type filter to the <i>front</i> of the exclusion list.
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    /**
     * Reset the configured type filters.
     *
     * @param useDefaultFilters whether to re-register the default filters for
     *                          the {@link Component @Component}, {@link Repository @Repository},
     *                          {@link Service @Service}, and {@link Controller @Controller}
     *                          stereotype annotations
     * @see #registerDefaultFilters()
     */
    public void resetFilters() {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

    /**
     * Set the {@link ResourceLoader} to use for resource locations.
     * This will typically be a {@link ResourcePatternResolver} implementation.
     * <p>Default is a {@code PathMatchingResourcePatternResolver}, also capable of
     * resource pattern resolving through the {@code ResourcePatternResolver} interface.
     *
     * @see org.springframework.core.io.support.ResourcePatternResolver
     * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    /**
     * Return the ResourceLoader that this component provider uses.
     */
    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    /**
     * Set the {@link MetadataReaderFactory} to use.
     * <p>Default is a {@link CachingMetadataReaderFactory} for the specified
     * {@linkplain #setResourceLoader resource loader}.
     * <p>Call this setter method <i>after</i> {@link #setResourceLoader} in order
     * for the given MetadataReaderFactory to override the default factory.
     */
    public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    /**
     * Return the MetadataReaderFactory used by this component provider.
     */
    public final MetadataReaderFactory getMetadataReaderFactory() {
        return this.metadataReaderFactory;
    }

    /**
     * Set the Environment to use when resolving placeholders and evaluating
     * {@link Conditional @Conditional}-annotated component classes.
     * <p>The default is a {@link StandardEnvironment}.
     *
     * @param environment the Environment to use
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public final Environment getEnvironment() {
        return this.environment;
    }

    /**
     * Scan the class path for candidate components.
     *
     * @param basePackage the package to check for annotated classes
     * @return a corresponding Set of autodetected bean definitions
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            boolean traceEnabled = logger.isTraceEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            for (Resource resource : resources) {
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                        if (isCandidateComponent(metadataReader)) {
                            ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                            sbd.setResource(resource);
                            sbd.setSource(resource);
                            if (isCandidateComponent(sbd)) {
                                if (debugEnabled) {
                                    logger.debug("Identified candidate component class: " + resource);
                                }
                                candidates.add(sbd);
                            } else {
                                if (debugEnabled) {
                                    logger.debug("Ignored because not a concrete top-level class: " + resource);
                                }
                            }
                        } else {
                            if (traceEnabled) {
                                logger.trace("Ignored because not matching any filter: " + resource);
                            }
                        }
                    } catch (Throwable ex) {
                        throw new BeanDefinitionStoreException("Failed to read candidate component class: " + resource,
                                ex);
                    }
                } else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: " + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

    /**
     * Resolve the specified base package into a pattern specification for
     * the package search path.
     * <p>The default implementation resolves placeholders against system properties,
     * and converts a "."-based package path to a "/"-based resource path.
     *
     * @param basePackage the base package as specified by the user
     * @return the pattern specification to be used for package searching
     */
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    /**
     * Determine whether the given class does not match any exclude filter
     * and does match at least one include filter.
     *
     * @param metadataReader the ASM ClassReader for the class
     * @return whether the class qualifies as a candidate component
     */
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the given bean definition qualifies as candidate.
     * <p>The default implementation checks whether the class is not an interface
     * and not dependent on an enclosing class.
     * <p>Can be overridden in subclasses.
     *
     * @param beanDefinition the bean definition to check
     * @return whether the bean definition qualifies as a candidate component
     */
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isIndependent()
                && (metadata.isConcrete()
                || (metadata.isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))));
    }

    /**
     * Clear the underlying metadata cache, removing all cached class metadata.
     */
    public void clearCache() {
        if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
            ((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
        }
    }

}
