package weilianglol.mimosa.java.compiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import weilianglol.mimosa.java.exception.CompilationException;

public class TacticalCompiler extends GenericCompiler {

	public TacticalCompiler(String className, String code) throws IOException, CompilationException {
		super(className, code);
	}

	public void createInstance(Object... params) throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		super.createInstance(mapParameterClass(params), params);
	}

	public Object renderMethod(String methodName, Object... params) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return super.renderMethod(methodName, mapParameterClass(params), params);
	}

	public <T> T renderMethod(String methodName, Class<T> returnType, Object... params) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return super.renderMethod(methodName, returnType, mapParameterClass(params), params);
	}

	private Class<?>[] mapParameterClass(Object... params) {
		return Arrays.asList(params).stream().map(param -> param.getClass()).toArray(Class<?>[]::new);
	}

}
