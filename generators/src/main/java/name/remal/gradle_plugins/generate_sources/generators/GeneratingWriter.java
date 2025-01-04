package name.remal.gradle_plugins.generate_sources.generators;

import com.google.common.io.CharStreams;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import javax.annotation.WillNotClose;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class GeneratingWriter extends BufferedWriter {

    private final String lineSeparator;

    public GeneratingWriter(OutputStream out, Charset charset, String lineSeparator) {
        super(newWriter(out, charset));
        this.lineSeparator = lineSeparator;
    }

    @SneakyThrows
    private static Writer newWriter(OutputStream out, Charset charset) {
        return new OutputStreamWriter(out, charset);
    }

    @Override
    public void newLine() throws IOException {
        write(lineSeparator);
    }

    public void copy(@WillNotClose Reader reader) throws IOException {
        synchronized (lock) {
            CharStreams.copy(reader, this);
        }
    }

}
