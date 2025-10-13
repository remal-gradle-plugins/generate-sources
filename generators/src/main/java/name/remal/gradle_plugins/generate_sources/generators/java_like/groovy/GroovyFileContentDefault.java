package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeStaticImportsChunkDefault;
import org.jspecify.annotations.Nullable;

public class GroovyFileContentDefault
    extends GroovyContentDefault
    implements GroovyFileContent {

    public GroovyFileContentDefault(
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        addLastChunks(
            JavaLikeStaticImportsChunkDefault.builder()
                .statementDelimiter("")
                .build(),
            JavaLikeImportsChunkDefault.builder()
                .statementDelimiter("")
                .build()
        );
    }

    public GroovyFileContentDefault() {
        this(null, null);
    }

}
