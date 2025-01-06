package name.remal.gradle_plugins.generate_sources.task;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaClassFileContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaClassFileContentDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaFileContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.java.JavaFileContentDefault;
import org.gradle.api.tasks.CacheableTask;

@CacheableTask
public abstract class GenerateJava
    extends AbstractGenerateJavaLike<JavaContent, JavaFileContent, JavaClassFileContent> {

    @Override
    protected JavaFileContent createFileContent(
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        return new JavaFileContentDefault(indent, lineSeparator);
    }

    @Override
    protected String getClassFileExtension() {
        return "java";
    }

    @Override
    protected JavaClassFileContent createClassFileContent(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        return new JavaClassFileContentDefault(packageName, simpleName, indent, lineSeparator);
    }

}
