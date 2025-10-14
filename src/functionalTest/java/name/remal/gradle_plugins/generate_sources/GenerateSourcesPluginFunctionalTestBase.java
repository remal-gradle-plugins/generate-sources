package name.remal.gradle_plugins.generate_sources;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PROTECTED;
import static name.remal.gradle_plugins.toolkit.GradleVersionUtils.isCurrentGradleVersionLessThan;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.errorprone.annotations.ForOverride;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import name.remal.gradle_plugins.toolkit.testkit.functional.AbstractGradleProject;
import name.remal.gradle_plugins.toolkit.testkit.functional.GradleKtsProject;
import name.remal.gradle_plugins.toolkit.testkit.functional.GradleProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor(access = PROTECTED)
abstract class GenerateSourcesPluginFunctionalTestBase<GradleProjectType extends AbstractGradleProject<?, ?, ?, ?>> {

    @ForOverride
    protected void executeBeforeEachActions() {
    }


    protected final GradleProjectType project;

    @BeforeEach
    protected final void beforeEach() {
        project.forBuildFile(build -> {
            build.applyPlugin("name.remal.generate-sources");
        });

        if (isCurrentGradleVersionLessThan("8.0")) {
            project.withoutConfigurationCache();
        }

        executeBeforeEachActions();
    }


    @Nested
    class JavaProject {

        @BeforeEach
        void beforeEach() {
            project.forBuildFile(build -> {
                build.applyPlugin("java");
            });
        }

        @Test
        void generateJava() {
            project.getBuildFile().block("generateSources.forMainSourceSet", forSourceSet -> {
                forSourceSet.block("java", java -> {
                    java.block("classFile(\"pkg\", \"Logic\")", classFile -> {
                        classFile.block("block(\"public class ${simpleName}\")", classBlock -> {
                            classBlock.line("addImport(\"java.util.List\")");
                            classBlock.block("block(\"public static List<Integer> execute()\")", methodBlock -> {
                                methodBlock.line("addStaticImport(\"java.util.Arrays\", \"asList\")");
                                methodBlock.line("line(\"return asList(1, 2, 3);\")");
                            });
                        });
                    });
                });
            });

            project.assertBuildSuccessfully("classes");

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
            project.getBuildFile().block("generateSources.forMainSourceSet", forSourceSet -> {
                forSourceSet.block("resources", resources -> {
                    resources.block("binaryFile(\"dir/file.bin\")", binaryFile -> {
                        if (project instanceof GradleProject) {
                            binaryFile.line("write([1, 2, 3] as byte[])");
                        } else if (project instanceof GradleKtsProject) {
                            binaryFile.line("write(byteArrayOf(1, 2, 3))");
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    });
                });
            });

            project.assertBuildSuccessfully("classes");

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
            project.getBuildFile().block("generateSources.forMainSourceSet", forSourceSet -> {
                forSourceSet.block("resources", resources -> {
                    resources.block("textFile(\"dir/file.txt\")", binaryFile -> {
                        binaryFile.line("write(\"123\")");
                    });
                });
            });

            project.assertBuildSuccessfully("classes");

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
