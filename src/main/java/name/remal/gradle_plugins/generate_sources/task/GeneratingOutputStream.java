package name.remal.gradle_plugins.generate_sources.task;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static name.remal.gradle_plugins.toolkit.PathUtils.createParentDirectories;

import com.google.common.io.ByteStreams;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.WillNotClose;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class GeneratingOutputStream
    extends BufferedOutputStream
    implements Generating {

    private final Path generatingPath;

    public GeneratingOutputStream(Path generatingPath) {
        super(newOutputStream(generatingPath));
        this.generatingPath = generatingPath;
    }

    @SneakyThrows
    private static OutputStream newOutputStream(Path path) {
        return Files.newOutputStream(
            createParentDirectories(path),
            CREATE,
            TRUNCATE_EXISTING,
            WRITE
        );
    }

    public synchronized void copy(@WillNotClose InputStream inputStream) throws IOException {
        ByteStreams.copy(inputStream, this);
    }

}
