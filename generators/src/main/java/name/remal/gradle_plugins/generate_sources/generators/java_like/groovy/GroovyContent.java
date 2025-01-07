package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static name.remal.gradle_plugins.toolkit.StringUtils.escapeGroovy;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import java.util.Objects;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;
import name.remal.gradle_plugins.toolkit.ObjectUtils;

public interface GroovyContent
    extends JavaLikeContent<GroovyContent> {

    @Override
    default String escapeString(String string) {
        return escapeGroovy(string);
    }

    @Override
    default void indent(
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.indent(closure);
    }

    @Override
    default void block(
        CharSequence statement,
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(statement, closure);
    }

    @Override
    default void block(
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(closure);
    }

    @Override
    default void suppressWarningsLine(String... warnings) {
        val content = new StringBuilder();
        content.append("@SuppressWarnings");
        if (ObjectUtils.isNotEmpty(warnings)) {
            content.append('(');
            if (warnings.length > 1) {
                content.append('[');
            }
            content.append(stream(warnings)
                .filter(Objects::nonNull)
                .map(warning -> '"' + escapeString(warning) + '"')
                .collect(joining(", "))
            );
            if (warnings.length > 1) {
                content.append(']');
            }
            content.append(')');
        }
        line(content);
    }

}
