package name.remal.gradle_plugins.generate_sources.generators;

import static java.lang.String.format;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;

public interface TextContent extends DelegatingCharSequence {

    void line(CharSequence line);

    @FormatMethod
    default void line(@FormatString String format, Object... args) {
        line(format(format, args));
    }

    default void line() {
        line("");
    }


    <T extends TextContentChunk> T getChunk(Class<T> type);

}
