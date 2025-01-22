package name.remal.gradle_plugins.generate_sources.generators;

import static name.remal.gradle_plugins.toolkit.ClosureUtils.configureUsing;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.FirstParam;
import org.gradle.api.Action;

public interface ScriptContent<Block extends ScriptContent<Block>>
    extends TextContent {

    /**
     * Escape the provided string so that it's safe to put it in the language string.
     *
     * <p>Example (escaping line separators):
     * <pre>{@code
     * line("return \"%s\";", escapeString("multi\nline\nstring"));
     * }</pre>
     * Generates:
     * <pre>{@code
     * return "multi\nline\nstring";
     * }</pre>
     */
    String escapeString(String string);

    /**
     * Indents block of code.
     *
     * <p>Example:
     * <pre>{@code
     * line("var list = List.of(");
     * indent(inner -> {
     *     inner.line("1,");
     *     inner.line("2,");
     *     inner.line("3");
     * })
     * line(");");
     * }</pre>
     * Generates:
     * <pre>{@code
     * var list = List.of(
     *     1,
     *     2,
     *     3
     * );
     * }</pre>
     */
    void indent(Action<Block> action);

    /**
     * Indents block of code.
     *
     * <p>Example:
     * <pre>{@code
     * line("var list = List.of(");
     * indent(inner -> {
     *     inner.line("1,");
     *     inner.line("2,");
     *     inner.line("3");
     * })
     * line(");");
     * }</pre>
     * Generates:
     * <pre>{@code
     * var list = List.of(
     *     1,
     *     2,
     *     3
     * );
     * }</pre>
     */
    default void indent(
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        indent(configureUsing(closure));
    }

    /**
     * Prints a "block" of code.
     * A "block" is a statement that has child statements.
     *
     * <p>Example:
     * <pre>{@code
     * block("if (condition)", inner -> {
     *     inner.line("return true;");
     * })
     * }</pre>
     * Generates:
     * <pre>{@code
     * if (condition) {
     *     return true;
     * }
     * }</pre>
     *
     * <p>Curly braces are added automatically.
     */
    void block(
        CharSequence statement,
        Action<Block> action
    );

    /**
     * Prints a "block" of code.
     * A "block" is a statement that has child statements.
     *
     * <p>Example:
     * <pre>{@code
     * block("if (condition)", inner -> {
     *     inner.line("return true;");
     * })
     * }</pre>
     * Generates:
     * <pre>{@code
     * if (condition) {
     *     return true;
     * }
     * }</pre>
     *
     * <p>Curly braces are added automatically.
     */
    default void block(
        CharSequence statement,
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        block(statement, configureUsing(closure));
    }

    /**
     * Prints a "block" of code.
     * A "block" is a statement that has child statements.
     *
     * <p>Example:
     * <pre>{@code
     * block(inner -> {
     *     inner.line("return true;");
     * })
     * }</pre>
     * Generates:
     * <pre>{@code
     * {
     *     return true;
     * }
     * }</pre>
     *
     * <p>Curly braces are added automatically.
     */
    default void block(
        Action<Block> action
    ) {
        block("", action);
    }

    /**
     * Prints a "block" of code.
     * A "block" is a statement that has child statements.
     *
     * <p>Example:
     * <pre>{@code
     * block(inner -> {
     *     inner.line("return true;");
     * })
     * }</pre>
     * Generates:
     * <pre>{@code
     * {
     *     return true;
     * }
     * }</pre>
     *
     * <p>Curly braces are added automatically.
     */
    default void block(
        @DelegatesTo(ScriptContent.class)
        @ClosureParams(FirstParam.FirstGenericType.class)
        Closure<?> closure
    ) {
        block(configureUsing(closure));
    }

}
