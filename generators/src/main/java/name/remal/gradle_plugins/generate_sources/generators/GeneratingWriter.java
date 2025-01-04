package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.format;

import com.google.common.io.CharStreams;
import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.WillNotClose;
import lombok.Getter;

@Getter
public class GeneratingWriter extends BufferedWriter {

    private final String lineSeparator;

    public GeneratingWriter(OutputStream out, Charset charset, String lineSeparator) {
        super(new OutputStreamWriter(out, charset));
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void newLine() throws IOException {
        write(lineSeparator);
    }

    @FormatMethod
    public void writeFormat(@FormatString String format, Object... args) throws IOException {
        write(format(format, args));
    }

    @FormatMethod
    public void writeLine(@FormatString String format, Object... args) throws IOException {
        writeFormat(format, args);
        newLine();
    }

    public void copy(@WillNotClose Reader reader) throws IOException {
        synchronized (lock) {
            CharStreams.copy(reader, this);
        }
    }

}
