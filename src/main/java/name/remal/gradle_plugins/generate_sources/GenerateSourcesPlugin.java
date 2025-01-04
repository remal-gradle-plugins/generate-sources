package name.remal.gradle_plugins.generate_sources;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.doNotInline;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class GenerateSourcesPlugin implements Plugin<Project> {

    public static final String GENERATE_SOURCES_EXTENSION_NAME = doNotInline("generateSources");

    @Override
    public void apply(Project project) {
        project.getExtensions().create(GENERATE_SOURCES_EXTENSION_NAME, GenerateSourcesExtension.class);
    }

}
