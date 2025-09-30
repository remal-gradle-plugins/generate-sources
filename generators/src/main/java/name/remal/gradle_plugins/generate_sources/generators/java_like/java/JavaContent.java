package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static name.remal.gradle_plugins.toolkit.StringUtils.escapeJava;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import java.util.Objects;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;
import org.jspecify.annotations.Nullable;

public interface JavaContent
    extends JavaLikeContent<JavaContent> {

    @Override
    default String escapeString(String string) {
        return escapeJava(string);
    }

    @Override
    default void indent(
        @DelegatesTo(JavaContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.indent(closure);
    }

    @Override
    default void block(
        CharSequence statement,
        @DelegatesTo(JavaContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(statement, closure);
    }

    @Override
    default void block(
        @DelegatesTo(JavaContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(closure);
    }

    @Override
    @SuppressWarnings("java:S2259")
    default void suppressWarningsLine(@Nullable String... warnings) {
        var content = new StringBuilder();
        content.append("@SuppressWarnings");
        if (warnings.length > 0) {
            content.append('(');
            if (warnings.length > 1) {
                content.append('{');
            }
            content.append(
                stream(warnings)
                    .filter(Objects::nonNull)
                    .map(warning -> '"' + escapeString(warning) + '"')
                    .collect(joining(", "))
            );
            if (warnings.length > 1) {
                content.append('}');
            }
            content.append(')');
        }
        line(content);
    }

}
