package weilianglol.mimosa.node.compiler;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetClosable;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.values.V8Value;

import lombok.Getter;
import weilianglol.mimosa.node.exception.ExecutionException;
import weilianglol.mimosa.node.exception.NodeNativeException;
import weilianglol.mimosa.node.filemanager.ScriptManager;

public class BaseNodeCompiler implements Closeable {

	protected @Getter NodeRuntime nodejs;
	protected @Getter List<IJavetClosable> exports;

	protected @Getter Path tempDir;
	protected @Getter List<Path> scripts;

	public BaseNodeCompiler(String id) throws IOException, InterruptedException {
		try {
			exports = new ArrayList<>();
			nodejs = V8Host.getNodeInstance().createV8Runtime(id);
			nodejs.allowEval(true);

			scripts = new ArrayList<>();
			tempDir = ScriptManager.generateDirectory(Paths.get(".mimosa/node/"), id);

			String uuid = tempDir.getFileName().toString();
			nodejs.getGlobalObject().set("__mms", ".mimosa/node/" + uuid);
			nodejs.getGlobalObject().set("__pkg", ".mimosa/node/node_modules");
		} catch (JavetException e) {
			throw new NodeNativeException("Failed to create compiler instance!", e);
		}
	}

	public Path load(String name, String code) throws FileNotFoundException {
		Path script = ScriptManager.createScript(tempDir, name, code);
		scripts.add(script);
		return script;
	}

	protected <T extends V8Value> T execute(String name, String script) {
		try {
			T object = nodejs.getExecutor(script).setResourceName("./" + name).execute();
			exports.add(object);
			return object;
		} catch (JavetException e) {
			throw new ExecutionException("Failed to execute resource!", e);
		}
	}

	public <T extends V8Value> T execute(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("const process = require('process');");
		sb.append("process.on('uncaughtException', (err, origin) => {});");
		sb.append("let ret = require(`./${__mms}/" + name + "`);");
		sb.append("ret;");
		return execute("exec-" + name, sb.toString());
	}

	@Override
	public void close() throws IOException {
		try {
			nodejs.terminateExecution();
			for (IJavetClosable export : exports)
				export.close();
			nodejs.close();
		} catch (JavetException e) {
			throw new NodeNativeException("Failed to terminate NodeJS instance!", e);
		}
		
		for (Path script : scripts)
			Files.delete(script);
		Files.delete(tempDir);
	}

}
