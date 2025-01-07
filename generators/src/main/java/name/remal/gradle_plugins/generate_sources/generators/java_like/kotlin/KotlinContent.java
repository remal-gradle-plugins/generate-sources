package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static name.remal.gradle_plugins.toolkit.StringUtils.escapeKotlin;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import java.util.Objects;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;
import name.remal.gradle_plugins.toolkit.ObjectUtils;

public interface KotlinContent
    extends JavaLikeContent<KotlinContent> {

    @Override
    default String escapeString(String string) {
        return escapeKotlin(string);
    }

    @Override
    default void indent(
        @DelegatesTo(KotlinContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin.KotlinContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.indent(closure);
    }

    @Override
    default void block(
        CharSequence statement,
        @DelegatesTo(KotlinContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin.KotlinContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(statement, closure);
    }

    @Override
    default void block(
        @DelegatesTo(KotlinContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin.KotlinContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(closure);
    }

    @Override
    default void suppressWarningsLine(String... warnings) {
        val content = new StringBuilder();
        content.append("@Suppress");
        if (ObjectUtils.isNotEmpty(warnings)) {
            content.append('(');
            content.append(stream(warnings)
                .filter(Objects::nonNull)
                .map(warning -> '"' + escapeString(warning) + '"')
                .collect(joining(", "))
            );
            content.append(')');
        }
        line(content);
    }

}
