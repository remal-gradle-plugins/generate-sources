package name.remal.gradle_plugins.generate_sources.generators.content;

import static java.lang.String.format;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;

public interface TextContent {

    @Override
    String toString();

    void line(String line);

    @FormatMethod
    default void line(@FormatString String format, Object... args) {
        line(format(format, args));
    }

}
