package weilianglol.mimosa.node.filemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class ScriptManager {

	public static Path generateDirectory(Path dir, String name) throws IOException {
		String uuid = UUID.nameUUIDFromBytes(name.getBytes()).toString();
		return Files.createDirectories(dir.resolve(uuid));
	}

	public static Path createScript(Path dir, String name, String code) throws FileNotFoundException {
		Path script = dir.resolve(name);
		try (PrintWriter pw = new PrintWriter(script.toFile())) {
			pw.println(code);
		}
		return script;
	}

}
