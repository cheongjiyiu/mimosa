package weilianglol.mimosa.stream;

import java.io.PrintStream;

public class CompilationStream {

	private static FilterStream filterStream;

	public static FilterStream getStream() {
		if (filterStream == null) {
			filterStream = new FilterStream(System.out);
			System.setOut(new PrintStream(filterStream));
		}

		return filterStream;
	}

}
