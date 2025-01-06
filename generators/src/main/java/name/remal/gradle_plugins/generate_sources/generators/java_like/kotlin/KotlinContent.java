package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;

public interface KotlinContent
    extends JavaLikeContent<KotlinContent> {

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

}
