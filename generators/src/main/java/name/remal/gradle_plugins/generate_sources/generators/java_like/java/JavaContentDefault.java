package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import name.remal.gradle_plugins.generate_sources.generators.java_like.AbstractJavaLikeContent;
import org.jspecify.annotations.Nullable;

public class JavaContentDefault
    extends AbstractJavaLikeContent<JavaContent>
    implements JavaContent {

    public JavaContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public JavaContentDefault() {
        this(null, null);
    }

    @Override
    protected JavaContent newBlock() {
        return new JavaContentDefault(indent, lineSeparator);
    }

}
