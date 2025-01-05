package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import javax.annotation.Nullable;
import lombok.Getter;

public class JavaClassFileContentDefault
    extends JavaFileContentDefault
    implements JavaClassFileContent {

    @Getter
    private final String simpleName;

    public JavaClassFileContentDefault(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
        this.simpleName = simpleName;
    }

    public JavaClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
