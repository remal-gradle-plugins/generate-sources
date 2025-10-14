package name.remal.gradle_plugins.generate_sources.task;

import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeFileContent;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface FileContentFactory<FileContent extends JavaLikeFileContent<?>> {

    FileContent createFileContent(
        @Nullable String indent,
        @Nullable String lineSeparator
    );

}
