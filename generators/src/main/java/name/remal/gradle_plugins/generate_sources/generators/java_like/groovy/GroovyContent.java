package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;

public interface GroovyContent
    extends JavaLikeContent<GroovyContent> {

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

}
