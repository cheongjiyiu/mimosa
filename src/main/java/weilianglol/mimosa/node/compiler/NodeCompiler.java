package weilianglol.mimosa.node.compiler;

import java.io.IOException;
import java.util.List;

import weilianglol.mimosa.stream.CompilationStream;
import weilianglol.mimosa.stream.FilterStream;

public class NodeCompiler extends BaseNodeCompiler {

	private FilterStream filterStream;
	
	public NodeCompiler(String id) throws IOException, InterruptedException {
		super(id);
		this.filterStream = CompilationStream.getStream();
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
		super.close();
	}
	
}
