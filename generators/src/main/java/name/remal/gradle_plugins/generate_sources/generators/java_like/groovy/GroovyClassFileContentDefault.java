package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;
import org.jspecify.annotations.Nullable;

public class GroovyClassFileContentDefault
    extends GroovyFileContentDefault
    implements GroovyClassFileContent {

    @Getter
    private final String simpleName;

    public GroovyClassFileContentDefault(
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

    public GroovyClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
