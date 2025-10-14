package name.remal.gradle_plugins.generate_sources.task;

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
    protected String getClassFileExtension() {
        return "java";
    }

    @Override
    protected FileContentFactory<JavaFileContent> getFileContentFactory() {
        return JavaFileContentDefault::new;
    }

    @Override
    protected ClassFileContentFactory<JavaClassFileContent> getClassFileContentFactory() {
        return JavaClassFileContentDefault::new;
    }

}
