package weilianglol.mimosa.node.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;

import weilianglol.mimosa.node.exception.NodeNativeException;

public class NativeNodeCompiler extends BaseNodeCompiler {
	
	protected JavetStandardConsoleInterceptor console;
	
	private ByteArrayOutputStream byteStream;
	private PrintStream printStream;
	
	public NativeNodeCompiler(String id) throws IOException, InterruptedException, JavetException {
		super(id);
		byteStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteStream);
		
		console = new JavetStandardConsoleInterceptor(nodejs);
		console.register(nodejs.getGlobalObject());
		hookStdOut();
	}
	
	private void hookStdOut() {
		console.setDebug(printStream);
		console.setError(printStream);
		console.setInfo(printStream);
		console.setLog(printStream);
		console.setTrace(printStream);
		console.setWarn(printStream);
	}
	
	public List<String> getSystemOutput() {
		if (byteStream.toString().isEmpty())
			return new ArrayList<>();
		
		return Arrays.asList(byteStream.toString().split("[\n\r]"));
	}

	public void clearSystemOutput() {
		byteStream.reset();
	}
	
	@Override
	public void close() throws IOException {
		try {
			console.unregister(nodejs.getGlobalObject());
		} catch (JavetException e) {
			throw new NodeNativeException("Failed to terminate NodeJS instance!", e);
		}
		super.close();
		printStream.close();
	}
	
}
