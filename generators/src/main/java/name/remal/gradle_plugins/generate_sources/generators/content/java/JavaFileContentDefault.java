package name.remal.gradle_plugins.generate_sources.generators.content.java;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeFileContent;

public class JavaFileContentDefault
    extends AbstractJavaLikeFileContent<JavaContent>
    implements JavaFileContent {

    public JavaFileContentDefault(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
    }

    public JavaFileContentDefault(@Nullable String packageName) {
        super(packageName);
    }

    @Override
    protected JavaContent newBlock() {
        return new JavaContentDefault(indent, lineSeparator);
    }

}
