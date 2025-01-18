package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import lombok.val;
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


    /**
     * Generate multiple lines delimited with a line separator.
     */
    default void lines(Iterable<? extends CharSequence> lines) {
        for (val line : lines) {
            if (line != null) {
                line(line);
            }
        }
    }

    /**
     * Generate multiple lines delimited with a line separator.
     */
    default void lines(CharSequence... lines) {
        lines(asList(lines));
    }


    /**
     * Return {@code true} if this {@link TextContent} has chunks.
     * {@code false} means that this {@link TextContent} is empty.
     */
    boolean hasChunks();

    @ApiStatus.Experimental
    <T extends TextContentChunk> T getChunk(Class<T> type);

}
