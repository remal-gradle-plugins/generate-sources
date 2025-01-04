package name.remal.gradle_plugins.generate_sources.generators.content;

import static java.lang.String.format;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;

public interface TextContent extends CharSequence {

    void line(CharSequence line);

    @FormatMethod
    default void line(@FormatString String format, Object... args) {
        line(format(format, args));
    }


    @Override
    default int length() {
        return toString().length();
    }

    @Override
    default char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

}
