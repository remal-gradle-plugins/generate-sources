package name.remal.gradle_plugins.generate_sources.generators.java_like;

import name.remal.gradle_plugins.generate_sources.generators.AbstractScriptContent;
import org.jspecify.annotations.Nullable;

public abstract class AbstractJavaLikeContent<Block extends JavaLikeContent<Block>>
    extends AbstractScriptContent<Block>
    implements JavaLikeContent<Block> {

    public static final String DEFAULT_INDENT = "    ";


    protected AbstractJavaLikeContent(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    protected AbstractJavaLikeContent() {
        this(null, null);
    }

    @Override
    protected String getDefaultIndent() {
        return DEFAULT_INDENT;
    }

    @Override
    protected String getBlockStart() {
        return " {";
    }

    @Override
    protected String getBlockEnd() {
        return "}";
    }

}
