package org.frameworkset.http.converter.json;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.Assert;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class BBossHandlerInstantiator  extends HandlerInstantiator {

	private final BaseApplicationContext beanFactory;


	/**
	 * Create a new SpringHandlerInstantiator for the given BeanFactory.
	 * @param beanFactory the target BeanFactory
	 */
	public BBossHandlerInstantiator(BaseApplicationContext  beanFactory) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		this.beanFactory = beanFactory;
	}

	@Override
	public JsonSerializer<?> serializerInstance(SerializationConfig config,
			Annotated annotated, Class<?> keyDeserClass) {
		return (JsonSerializer<?>) this.beanFactory.createBean(keyDeserClass);
	}

	@Override
	public JsonDeserializer<?> deserializerInstance(DeserializationConfig config,
			Annotated annotated, Class<?> deserClass) {
		return (JsonDeserializer<?>) this.beanFactory.createBean(deserClass);
	}

	@Override
	public KeyDeserializer keyDeserializerInstance(DeserializationConfig config,
			Annotated annotated, Class<?> serClass) {
		return (KeyDeserializer) this.beanFactory.createBean(serClass);
	}

	@Override
	public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config,
			Annotated annotated, Class<?> resolverClass) {
		return (TypeResolverBuilder<?>) this.beanFactory.createBean(resolverClass);
	}

	@Override
	public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config,
			Annotated annotated, Class<?> resolverClass) {
		return (TypeIdResolver) this.beanFactory.createBean(resolverClass);
	}
}
