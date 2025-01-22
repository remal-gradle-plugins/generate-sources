package name.remal.gradle_plugins.generate_sources;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        void generateJava() {
            project.getBuildFile().appendBlock("generateSources.forSourceSet(sourceSets.main)", forSourceSet -> {
                forSourceSet.appendBlock("java", java -> {
                    java.appendBlock("classFile('pkg', 'Logic')", classFile -> {
                        classFile.appendBlock("block(\"public class ${simpleName}\")", classBlock -> {
                            classBlock.append("addImport('java.util.List')");
                            classBlock.appendBlock("block('public static List<Integer> execute()')", methodBlock -> {
                                methodBlock.append("addStaticImport('java.util.Arrays', 'asList')");
                                methodBlock.append("line('return asList(1, 2, 3);')");
                            });
                        });
                    });
                });
            });

            project.assertBuildSuccessfully();

            try {
                var mainClassesDir = project.getProjectDir().toPath()
                    .resolve("build/classes/java/main");
                var mainClassesDirUrl = mainClassesDir.toUri().toURL();
                try (var classLoader = new URLClassLoader(new URL[]{mainClassesDirUrl}, null)) {
                    var logicClass = classLoader.loadClass("pkg.Logic");
                    var logicMethod = logicClass.getMethod("execute");
                    var logicResult = logicMethod.invoke(null);
                    assertEquals(List.of(1, 2, 3), logicResult);
                }
            } catch (Throwable e) {
                throw new AssertionError("Assertion failed for " + project.getProjectDir(), e);
            }
        }

        @Test
        void generateResources_binary() {
            project.getBuildFile().appendBlock("generateSources.forSourceSet(sourceSets.main)", forSourceSet -> {
                forSourceSet.appendBlock("resources", resources -> {
                    resources.appendBlock("binaryFile('dir/file.bin')", binaryFile -> {
                        binaryFile.append("write([1, 2, 3] as byte[])");
                    });
                });
            });

            project.assertBuildSuccessfully();

            try {
                var processedResourcePath = project.getProjectDir().toPath()
                    .resolve("build/resources/main/dir/file.bin");
                assertThat(processedResourcePath)
                    .isRegularFile()
                    .hasBinaryContent(new byte[]{1, 2, 3});
            } catch (Throwable e) {
                throw new AssertionError("Assertion failed for " + project.getProjectDir(), e);
            }
        }

        @Test
        void generateResources_text() {
            project.getBuildFile().appendBlock("generateSources.forSourceSet(sourceSets.main)", forSourceSet -> {
                forSourceSet.appendBlock("resources", resources -> {
                    resources.appendBlock("textFile('dir/file.txt')", binaryFile -> {
                        binaryFile.append("write('123')");
                    });
                });
            });

            project.assertBuildSuccessfully();

            try {
                var processedResourcePath = project.getProjectDir()
                    .toPath()
                    .resolve("build/resources/main/dir/file.txt");
                assertThat(processedResourcePath)
                    .isRegularFile()
                    .hasBinaryContent("123".getBytes(UTF_8));
            } catch (Throwable e) {
                throw new AssertionError("Assertion failed for " + project.getProjectDir(), e);
            }
        }

    }

}
