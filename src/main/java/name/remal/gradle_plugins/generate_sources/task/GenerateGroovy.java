package name.remal.gradle_plugins.generate_sources.task;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyClassFileContent;
import name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyClassFileContentDefault;
import name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyContent;
import name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyFileContent;
import name.remal.gradle_plugins.generate_sources.generators.content.groovy.GroovyFileContentDefault;
import org.gradle.api.tasks.CacheableTask;

@CacheableTask
public abstract class GenerateGroovy
    extends AbstractGenerateJavaLike<GroovyContent, GroovyFileContent, GroovyClassFileContent> {

    @Override
    protected GroovyFileContent createFileContent(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        return new GroovyFileContentDefault(packageName, indent, lineSeparator);
    }

    @Override
    protected String getClassFileExtension() {
        return "groovy";
    }

    @Override
    protected GroovyClassFileContent createClassFileContent(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        return new GroovyClassFileContentDefault(packageName, simpleName, indent, lineSeparator);
    }

}
