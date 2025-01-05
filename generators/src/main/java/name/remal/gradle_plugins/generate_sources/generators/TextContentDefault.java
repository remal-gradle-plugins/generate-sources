package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.join;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class TextContentDefault
    implements TextContent {

    public static final String DEFAULT_LINE_SEPARATOR = "\n";


    protected final String lineSeparator;

    public TextContentDefault(@Nullable String lineSeparator) {
        this.lineSeparator = isNotEmpty(lineSeparator) ? lineSeparator : DEFAULT_LINE_SEPARATOR;
    }

    public TextContentDefault() {
        this.lineSeparator = DEFAULT_LINE_SEPARATOR;
    }


    protected final List<CharSequence> chunks = new ArrayList<>();

    @Override
    public String toString() {
        return join("", chunks);
    }

    @Override
    public void line(CharSequence line) {
        chunks.add(line);
        chunks.add(lineSeparator);
    }

    @Override
    public <T extends TextContentChunk> T getChunk(Class<T> type) {
        return chunks.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No chunk of " + type));
    }

}
