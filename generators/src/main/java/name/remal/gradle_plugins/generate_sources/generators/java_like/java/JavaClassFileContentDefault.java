package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import javax.annotation.Nullable;
import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;

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
        super(indent, lineSeparator);
        addFirstChunks(
            JavaLikePackageChunkDefault.builder()
                .packageName(defaultValue(packageName))
                .build()
        );
        this.simpleName = simpleName;
    }

    public JavaClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
