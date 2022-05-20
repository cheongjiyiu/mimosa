package weilianglol.mimosa.java.compiler;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import weilianglol.mimosa.java.exception.CompilationException;
import weilianglol.mimosa.stream.CompilationStream;
import weilianglol.mimosa.stream.FilterStream;

public class Compiler extends TacticalCompiler implements Closeable {

	private FilterStream filterStream;

	public Compiler(String className, String code) throws IOException, CompilationException {
		super(className, code);
		this.filterStream = CompilationStream.getStream();
		filterStream.startListening();
	}

	public Compiler(String className, String code, FilterStream filterStream)
			throws IOException, CompilationException {
		super(className, code);
		this.filterStream = filterStream;
		filterStream.startListening();
	}

	public List<String> getSystemOutput() {
		return filterStream.getLines();
	}
	
	public void clearSystemOutput() {
		filterStream.clear();
	}

	@Override
	public void close() throws IOException {
		filterStream.stopListening();
	}

}
