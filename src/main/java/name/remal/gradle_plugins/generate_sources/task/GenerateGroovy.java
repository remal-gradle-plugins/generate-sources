package name.remal.gradle_plugins.generate_sources.task;

import name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyClassFileContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyClassFileContentDefault;
import name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyFileContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.groovy.GroovyFileContentDefault;
import org.gradle.api.tasks.CacheableTask;

@CacheableTask
public abstract class GenerateGroovy
    extends AbstractGenerateJavaLike<GroovyContent, GroovyFileContent, GroovyClassFileContent> {

    @Override
    protected String getClassFileExtension() {
        return "groovy";
    }

    @Override
    protected FileContentFactory<GroovyFileContent> getFileContentFactory() {
        return GroovyFileContentDefault::new;
    }

    @Override
    protected ClassFileContentFactory<GroovyClassFileContent> getClassFileContentFactory() {
        return GroovyClassFileContentDefault::new;
    }

}
