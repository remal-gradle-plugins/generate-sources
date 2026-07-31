package name.remal.gradle_plugins.generate_sources;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.doNotInline;

import name.remal.gradle_plugins.toolkit.AbstractSettingsAwarePlugin;
import org.gradle.api.Project;

public abstract class GenerateSourcesPlugin extends AbstractSettingsAwarePlugin {

    public static final String GENERATE_SOURCES_EXTENSION_NAME = doNotInline("generateSources");

    @Override
    protected void applyToProject(Project project) {
        project.getExtensions().create(GENERATE_SOURCES_EXTENSION_NAME, GenerateSourcesExtension.class);
    }

}
