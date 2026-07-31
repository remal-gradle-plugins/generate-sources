package name.remal.gradle_plugins.generate_sources;

import lombok.RequiredArgsConstructor;
import name.remal.gradle_plugins.toolkit.testkit.functional.GradleProject;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class GenerateSourcesPluginAppliedViaSettingsFunctionalTest {

    private final GradleProject project;

    @Test
    void appliedViaSettingsIsAppliedToProject() {
        project.forSettingsFile(settings -> settings.applyPlugin("name.remal.generate-sources"));

        // The plugin must NOT be applied via the project's build file: it should reach the project
        // solely through the Settings-level application propagating via GradleLifecycle.beforeProject.
        // The assertion runs at configuration time (not inside doLast/doFirst), because reading
        // `project` at execution time is unsupported with the configuration cache, which the
        // functional test build runs with.
        project.getBuildFile().line(
            "tasks.register('assertPluginApplied') { assert"
                + " project.pluginManager.hasPlugin('name.remal.generate-sources') }"
        );

        project.assertBuildSuccessfully("assertPluginApplied");
    }

}
