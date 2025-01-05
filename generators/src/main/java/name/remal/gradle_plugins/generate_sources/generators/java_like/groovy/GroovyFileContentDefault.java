package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import static java.util.Arrays.asList;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;

import javax.annotation.Nullable;
import lombok.Getter;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeStaticImportsChunkDefault;

public class GroovyFileContentDefault
    extends GroovyContentDefault
    implements GroovyFileContent {

    @Getter
    private final String packageName;

    public GroovyFileContentDefault(
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
            JavaLikeStaticImportsChunkDefault.builder()
                .statementDelimiter("")
                .build(),
            JavaLikeImportsChunkDefault.builder()
                .statementDelimiter("")
                .build()
        ));
    }

    public GroovyFileContentDefault(@Nullable String packageName) {
        this(packageName, null, null);
    }

}
