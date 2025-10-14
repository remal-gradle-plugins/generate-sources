package name.remal.gradle_plugins.generate_sources;

import name.remal.gradle_plugins.toolkit.testkit.functional.GradleKtsProject;

class GenerateSourcesPluginKtsFunctionalTest extends GenerateSourcesPluginFunctionalTestBase<GradleKtsProject> {

    GenerateSourcesPluginKtsFunctionalTest(GradleKtsProject project) {
        super(project);
    }

    @Override
    protected void executeBeforeEachActions() {
        // Kotlin-DSL *tests* fail with Configuration Cache, but these scenarios work in a real project
        project.withoutConfigurationCache();
    }

}
