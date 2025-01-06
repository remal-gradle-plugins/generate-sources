package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;

public class KotlinFileContentDefault
    extends KotlinContentDefault
    implements KotlinFileContent {

    public KotlinFileContentDefault(
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        addLastChunks(
            JavaLikeImportsChunkDefault.builder()
                .statementDelimiter("")
                .build()
        );
    }

    public KotlinFileContentDefault() {
        this(null, null);
    }

}
