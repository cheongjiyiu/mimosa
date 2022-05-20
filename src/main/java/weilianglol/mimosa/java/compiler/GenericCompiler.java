package weilianglol.mimosa.java.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import com.athaydes.osgiaas.javac.JavacService;
import com.athaydes.osgiaas.javac.internal.DefaultClassLoaderContext;

import weilianglol.mimosa.java.exception.CompilationException;

public class GenericCompiler {

	private Class<?> clazz;
	private Object object;

	public GenericCompiler(String className, String code) throws IOException, CompilationException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos)) {
			this.clazz = JavacService.createDefault()
					.compileJavaClass(DefaultClassLoaderContext.INSTANCE, className, code, ps)
					.orElseThrow(() -> new CompilationException(baos));
		}
	}

	public Object getInstance() {
		return object;
	}

	public <T extends Object> T getInstance(Class<T> type) {
		return type.cast(object);
	}

	public void createInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		object = clazz.getDeclaredConstructor().newInstance();
	}

	public void createInstance(Class<?>[] classes, Object[] params)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		object = clazz.getConstructor(classes).newInstance(params);
	}

	public Object renderMethod(String methodName, Class<?>[] classes, Object[] params) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getMethod(methodName, classes).invoke(object, params);
	}

	public <T extends Object> T renderMethod(String methodName, Class<T> returnType, Class<?>[] classes,
			Object[] params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Object ret = renderMethod(methodName, classes, params);
		return ret == null ? returnType.cast(ret) : null;
	}

}
