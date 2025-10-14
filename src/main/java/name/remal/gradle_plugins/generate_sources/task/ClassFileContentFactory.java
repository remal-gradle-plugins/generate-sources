package name.remal.gradle_plugins.generate_sources.task;

import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeClassFileContent;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ClassFileContentFactory<ClassFileContent extends JavaLikeClassFileContent<?>> {

    ClassFileContent createClassFileContent(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    );

}
