package name.remal.gradle_plugins.generate_sources.generators.content.groovy;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import name.remal.gradle_plugins.generate_sources.generators.content.JavaLikeContent;

public interface GroovyContent
    extends JavaLikeContent<GroovyContent> {

    @Override
    default void indent(
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.indent(closure);
    }

    @Override
    default void block(
        CharSequence string,
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(string, closure);
    }

    @Override
    default void block(
        @DelegatesTo(GroovyContent.class)
        @ClosureParams(
            value = SimpleType.class,
            options = "name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyContent"
        )
        Closure<?> closure
    ) {
        JavaLikeContent.super.block(closure);
    }

}
