package weilianglol.mimosa.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class SplitStream extends OutputStream {

	private List<OutputStream> outputStreams;

	public SplitStream(List<OutputStream> outputStreams) {
		this.outputStreams = outputStreams;
	}

	public SplitStream(OutputStream... outputStreams) {
		this(Arrays.asList(outputStreams));
	}
	
	public void write(int b) throws IOException {
		for (OutputStream os : outputStreams)
			os.write(b);
	}

	public void flush() throws IOException {
		for (OutputStream os : outputStreams)
			os.flush();
	}

	public void close() throws IOException {
		for (OutputStream os : outputStreams)
			os.close();
	}

}
