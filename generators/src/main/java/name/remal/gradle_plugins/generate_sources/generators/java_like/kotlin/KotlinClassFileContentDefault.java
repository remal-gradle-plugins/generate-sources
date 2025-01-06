package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import javax.annotation.Nullable;
import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;

public class KotlinClassFileContentDefault
    extends KotlinFileContentDefault
    implements KotlinClassFileContent {

    @Getter
    private final String simpleName;

    public KotlinClassFileContentDefault(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        addFirstChunks(
            JavaLikePackageChunkDefault.builder()
                .packageName(defaultValue(packageName))
                .statementDelimiter("")
                .build()
        );
        this.simpleName = simpleName;
    }

    public KotlinClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
