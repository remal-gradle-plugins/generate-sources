package name.remal.gradle_plugins.generate_sources;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import name.remal.gradle_plugins.toolkit.testkit.functional.GradleProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class GenerateSourcesPluginFunctionalTest {

    final GradleProject project;

    @BeforeEach
    void beforeEach() {
        project.forBuildFile(build -> {
            build.applyPlugin("name.remal.generate-sources");
        });
    }


    @Nested
    class Java {

        @BeforeEach
        void beforeEach() {
            project.forBuildFile(build -> {
                build.applyPlugin("java");
                build.registerDefaultTask("classes");
            });
        }

        @Test
        void generateResources_binary() {
            project.getBuildFile().appendBlock("generateSources.forSourceSet(sourceSets.main)", forSourceSet -> {
                forSourceSet.appendBlock("resources", resources -> {
                    resources.appendBlock("binaryFile('dir/file.bin')", binaryFile -> {
                        binaryFile.append("write([1, 2, 3] as byte[]);");
                    });
                });
            });

            project.assertBuildSuccessfully();

            val processedResourcePath = project.getProjectDir().toPath().resolve("build/resources/main/dir/file.bin");
            assertThat(processedResourcePath)
                .isRegularFile()
                .hasBinaryContent(new byte[]{1, 2, 3});
        }

        @Test
        void generateResources_text() {
            project.getBuildFile().appendBlock("generateSources.forSourceSet(sourceSets.main)", forSourceSet -> {
                forSourceSet.appendBlock("resources", resources -> {
                    resources.appendBlock("textFile('dir/file.txt')", binaryFile -> {
                        binaryFile.append("write('123');");
                    });
                });
            });

            project.assertBuildSuccessfully();

            val processedResourcePath = project.getProjectDir().toPath().resolve("build/resources/main/dir/file.txt");
            assertThat(processedResourcePath)
                .isRegularFile()
                .hasBinaryContent("123".getBytes(UTF_8));
        }

    }

}
