package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunkDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeStaticImportsChunkDefault;

public class JavaFileContentDefault
    extends JavaContentDefault
    implements JavaFileContent {

    public JavaFileContentDefault(
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        addLastChunks(
            JavaLikeStaticImportsChunkDefault.builder()
                .build(),
            JavaLikeImportsChunkDefault.builder()
                .build()
        );
    }

    public JavaFileContentDefault() {
        this(null, null);
    }

}
