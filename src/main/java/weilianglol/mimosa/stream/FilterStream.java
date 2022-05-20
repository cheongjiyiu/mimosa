package weilianglol.mimosa.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilterStream extends OutputStream {

	private PrintStream origin;
	private HashMap<Thread, ByteArrayOutputStream> threadStreams;

	public FilterStream(PrintStream origin) {
		this.origin = origin;
		this.threadStreams = new HashMap<>();
	}

	public void startListening() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		threadStreams.put(Thread.currentThread(), byteStream);
	}

	public List<String> getLines() {
		ByteArrayOutputStream stream = threadStreams.get(Thread.currentThread());
		
		if (stream.toString().isEmpty())
			return new ArrayList<>();
		
		return Arrays.asList(stream.toString().split("[\n\r]"));
	}

	public void clear() {
		ByteArrayOutputStream stream = threadStreams.get(Thread.currentThread());
		stream.reset();
	}
	
	public void stopListening() {
		ByteArrayOutputStream stream = threadStreams.get(Thread.currentThread());
		
		try {
			stream.close();
		} catch (IOException e) {
		}

		threadStreams.remove(Thread.currentThread());
	}

	public void write(int b) throws IOException {
		if (threadStreams.containsKey(Thread.currentThread()))
			threadStreams.get(Thread.currentThread()).write(b);
		else
			origin.write(b);
	}

	public void flush() throws IOException {
		if (threadStreams.containsKey(Thread.currentThread()))
			threadStreams.get(Thread.currentThread()).flush();
		origin.flush();
	}

	public void close() throws IOException {
		for (ByteArrayOutputStream stream : threadStreams.values())
			stream.close();
		origin.close();
	}

}
