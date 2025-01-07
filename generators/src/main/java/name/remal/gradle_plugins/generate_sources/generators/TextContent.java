package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.format;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.jetbrains.annotations.ApiStatus;

public interface TextContent extends DelegatingCharSequence {

    /**
     * Generate a line (a string with a line separator).
     */
    void line(CharSequence line);

    /**
     * Generate a line (a string with a line separator).
     * See {@link String#format(String, Object...)}.
     */
    @FormatMethod
    default void line(@FormatString String format, Object... args) {
        line(format(format, args));
    }

    /**
     * Generate a line separator.
     */
    default void line() {
        line("");
    }


    @ApiStatus.Internal
    <T extends TextContentChunk> T getChunk(Class<T> type);

    boolean hasChunks();

}
