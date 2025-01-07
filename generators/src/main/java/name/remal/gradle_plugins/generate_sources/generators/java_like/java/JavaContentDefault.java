package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.AbstractJavaLikeContent;

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
    public JavaContent newBlock() {
        return new JavaContentDefault(indent, lineSeparator);
    }

}
