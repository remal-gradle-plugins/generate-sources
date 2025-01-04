package name.remal.gradle_plugins.generate_sources.generators.content;

import static name.remal.gradle_plugins.toolkit.ClosureUtils.configureUsing;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.FirstParam;
import org.gradle.api.Action;

public interface ScriptContent<Block extends ScriptContent<Block>>
    extends TextContent {

    void indent(Action<Block> action);

    default void indent(
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        indent(configureUsing(closure));
    }

    void block(
        CharSequence string,
        Action<Block> action
    );

    default void block(
        CharSequence string,
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        block(string, configureUsing(closure));
    }

    default void block(
        Action<Block> action
    ) {
        block("", action);
    }

    default void block(
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        block(configureUsing(closure));
    }

}
