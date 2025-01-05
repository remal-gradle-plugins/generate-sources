package name.remal.gradle_plugins.generate_sources.generators.java_like.koltin;

import static java.util.Arrays.asList;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import javax.annotation.Nullable;
import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;

public class KotlinFileContentDefault
    extends KotlinContentDefault
    implements KotlinFileContent {

    @Getter
    private final String packageName;

    public KotlinFileContentDefault(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        this.packageName = defaultValue(packageName);
        this.chunks.addAll(asList(
            JavaLikePackageChunkDefault.builder()
                .packageName(defaultValue(packageName))
                .statementDelimiter("")
                .build(),
            JavaLikeImportsChunkDefault.builder()
                .statementDelimiter("")
                .build()
        ));
    }

    public KotlinFileContentDefault(@Nullable String packageName) {
        this(packageName, null, null);
    }

}
