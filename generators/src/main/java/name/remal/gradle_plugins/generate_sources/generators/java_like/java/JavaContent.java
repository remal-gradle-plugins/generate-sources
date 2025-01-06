package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;

public interface JavaContent
    extends JavaLikeContent<JavaContent> {

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

}
