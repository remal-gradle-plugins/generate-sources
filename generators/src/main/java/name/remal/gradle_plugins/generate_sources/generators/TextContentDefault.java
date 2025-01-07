package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.join;
import static java.util.Arrays.asList;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import lombok.SneakyThrows;

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


    private final List<CharSequence> chunks = new ArrayList<>();

    private boolean canChunksBeAdded = true;

    protected final void addFirstChunks(CharSequence... chunks) {
        if (!canChunksBeAdded) {
            throw new IllegalStateException("At this moment, chunks can't be added."
                + " Check that the `ident()` or `block()` logic doesn't add any chink to the parent content."
            );
        }

        for (int i = 0; i < chunks.length; i++) {
            this.chunks.add(i, chunks[i]);
        }
    }

    protected final void addLastChunks(CharSequence... chunks) {
        if (!canChunksBeAdded) {
            throw new IllegalStateException("At this moment, chunks can't be added."
                + " Check that the `ident()` or `block()` logic doesn't add any chink to the parent content."
            );
        }

        this.chunks.addAll(asList(chunks));
    }

    @SneakyThrows
    protected final <T> T withoutChunksAdding(Callable<T> action) {
        canChunksBeAdded = false;
        try {
            return action.call();
        } finally {
            canChunksBeAdded = true;
        }
    }


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

    @Override
    public boolean hasChunks() {
        return !chunks.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        if (!hasChunks()) {
            return true;
        }
        return TextContent.super.isEmpty();
    }

}
