package com.pslib.jtool.framework.spring.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.pslib.jtool.framework.hutool.json.HutoolJsonUtil;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
/*
 * 
	<bean
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
		<property name="messageConverters">
			<list>
				<ref bean="jsonHttpMessageConverter" />
			</list>
		</property>
	</bean>
	<bean id="jsonHttpMessageConverter"
		class="com.pslib.jtool.framework.spring.converter.JsonHttpMessageConverter">
		<!--<property name="charset" value="UTF-8" />
		<property name="jsonConfig">
			<bean class="cn.hutool.json.JSONConfig">
				<property name="order" value="false" />
				<property name="ignoreError" value="false" />
				<property name="ignoreCase" value="false" />
				<property name="ignoreNullValue" value="true" />
				<property name="dateFormat" value="yyyy-MM-dd HH:mm:ss"></property>
			</bean>
		</property>-->
	</bean>

 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

	String charset = "UTF-8";
	JSONConfig jsonConfig = null;
	public JsonHttpMessageConverter() {
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.parseMediaType("application/json; charset="+charset));
		this.setSupportedMediaTypes(list);
		jsonConfig = HutoolJsonUtil.jsonConfig();
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	

	public JSONConfig getJsonConfig() {
		return jsonConfig;
	}
	public void setJsonConfig(JSONConfig jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return super.canRead(contextClass, mediaType);
	}

	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		return super.canWrite(clazz, mediaType);
	}

	/*
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#read(java.lang.reflect.Type, java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	public Object read(Type type, 
			Class<?> contextClass, 
			HttpInputMessage inputMessage 
	) throws IOException, HttpMessageNotReadableException {
		return readType(getType(type, contextClass), inputMessage);
	}

	/*
	 * @see org.springframework.http.converter.GenericHttpMessageConverter.write
	 */
	public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		super.write(o, contentType, outputMessage);// support StreamingHttpOutputMessage in spring4.0+
		// writeInternal(o, outputMessage);
	}

	/*
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	@Override
	protected Object readInternal(Class<? extends Object> clazz, //
			HttpInputMessage inputMessage //
	) throws IOException, HttpMessageNotReadableException {
		return readType(getType(clazz, null), inputMessage);
	}

	private Object readType(Type type, HttpInputMessage inputMessage) {

		try {
			InputStream in = inputMessage.getBody();
			if(jsonConfig!=null) {
				return JSONUtil.parseObj(IoUtil.read(in, charset), jsonConfig);
			}else { 
				return JSONUtil.parseObj(IoUtil.read(in, charset));
			}
		} catch (JSONException ex) {
			throw new JSONException("JSON parse error: " + ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new JSONException("I/O error while reading input message", ex);
		}
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

		ByteArrayOutputStream outnew = new ByteArrayOutputStream();
		try {
			//HttpHeaders headers = outputMessage.getHeaders();
			//headers.setContentLength(len);
			JSONObject json;
			if(jsonConfig!=null) {
				json = new JSONObject(object, jsonConfig);
			}else {
				json = new JSONObject(object);
			}

			outnew.write(json.toString().getBytes(charset));
			outnew.writeTo(outputMessage.getBody());

		} catch (JSONException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		} finally {
			outnew.close();
		}
	}
 
	protected Type getType(Type type, Class<?> contextClass) {
		if (Spring4TypeResolvableHelper.isSupport()) {
			return Spring4TypeResolvableHelper.getType(type, contextClass);
		}

		return type;
	}

	private static class Spring4TypeResolvableHelper {
		private static boolean hasClazzResolvableType;

		static {
			try {
				Class.forName("org.springframework.core.ResolvableType");
				hasClazzResolvableType = true;
			} catch (ClassNotFoundException e) {
				hasClazzResolvableType = false;
			}
		}

		private static boolean isSupport() {
			return hasClazzResolvableType;
		}

		@SuppressWarnings("rawtypes")
		private static Type getType(Type type, Class<?> contextClass) {
			if (contextClass != null) {
				ResolvableType resolvedType = ResolvableType.forType(type);
				if (type instanceof TypeVariable) {
					ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable) type, ResolvableType.forClass(contextClass));
					if (resolvedTypeVariable != ResolvableType.NONE) {
						return resolvedTypeVariable.resolve();
					}
				} else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
					ParameterizedType parameterizedType = (ParameterizedType) type;
					Class<?>[] generics = new Class[parameterizedType.getActualTypeArguments().length];
					Type[] typeArguments = parameterizedType.getActualTypeArguments();

					for (int i = 0; i < typeArguments.length; ++i) {
						Type typeArgument = typeArguments[i];
						if (typeArgument instanceof TypeVariable) {
							ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable) typeArgument, ResolvableType.forClass(contextClass));
							if (resolvedTypeArgument != ResolvableType.NONE) {
								generics[i] = resolvedTypeArgument.resolve();
							} else {
								generics[i] = ResolvableType.forType(typeArgument).resolve();
							}
						} else {
							generics[i] = ResolvableType.forType(typeArgument).resolve();
						}
					}

					return ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType();
				}
			}

			return type;
		}

		private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
			ResolvableType resolvedType;
			if (contextType.hasGenerics()) {
				resolvedType = ResolvableType.forType(typeVariable, contextType);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}

			ResolvableType superType = contextType.getSuperType();
			if (superType != ResolvableType.NONE) {
				resolvedType = resolveVariable(typeVariable, superType);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}
			for (ResolvableType ifc : contextType.getInterfaces()) {
				resolvedType = resolveVariable(typeVariable, ifc);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}
			return ResolvableType.NONE;
		}
	}

}
