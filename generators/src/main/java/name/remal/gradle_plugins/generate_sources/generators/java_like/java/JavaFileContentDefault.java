package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import static java.util.Arrays.asList;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import javax.annotation.Nullable;
import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeStaticImportsChunkDefault;

public class JavaFileContentDefault
    extends JavaContentDefault
    implements JavaFileContent {

    @Getter
    private final String packageName;

    public JavaFileContentDefault(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        this.packageName = defaultValue(packageName);
        this.chunks.addAll(asList(
            JavaLikePackageChunkDefault.builder()
                .packageName(defaultValue(packageName))
                .build(),
            JavaLikeStaticImportsChunkDefault.builder()
                .build(),
            JavaLikeImportsChunkDefault.builder()
                .build()
        ));
    }

    public JavaFileContentDefault(@Nullable String packageName) {
        this(packageName, null, null);
    }

}
