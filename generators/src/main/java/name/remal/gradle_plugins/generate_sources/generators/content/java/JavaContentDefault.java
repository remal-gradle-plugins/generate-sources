package name.remal.gradle_plugins.generate_sources.generators.content.java;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeContent;

public class JavaContentDefault
    extends AbstractJavaLikeContent<JavaContent>
    implements JavaContent {

    public JavaContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public JavaContentDefault() {
    }

    @Override
    protected JavaContent newBlock() {
        return new JavaContentDefault(indent, lineSeparator);
    }

}
