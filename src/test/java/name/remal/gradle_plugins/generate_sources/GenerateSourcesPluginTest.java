package name.remal.gradle_plugins.generate_sources;

import static java.util.stream.Collectors.toList;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.packageNameOf;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.unwrapGeneratedSubclass;
import static name.remal.gradle_plugins.toolkit.testkit.ProjectValidations.executeAfterEvaluateActions;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import name.remal.gradle_plugins.toolkit.testkit.TaskValidations;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class GenerateSourcesPluginTest {

    final Project project;

    @BeforeEach
    void beforeEach() {
        project.getPluginManager().apply(GenerateSourcesPlugin.class);
    }

    @Test
    void generateJavaDoesNotDependOnCompileJava() {
        project.getPluginManager().apply("java");
        val generateJava = project
            .getExtensions()
            .getByType(GenerateSourcesExtension.class)
            .forMainSourceSet()
            .java()
            .get();

        val generateJavaDependencies = generateJava
            .getTaskDependencies()
            .getDependencies(generateJava)
            .stream()
            .map(Task.class::cast)
            .collect(toList());

        val compileJava = project.getTasks().getByName("compileJava");
        assertThat(generateJavaDependencies)
            .doesNotContain(compileJava);
    }

    @Test
    void generateResourcesDoesNotDependOnProcessResources() {
        project.getPluginManager().apply("java");
        val generateResources = project
            .getExtensions()
            .getByType(GenerateSourcesExtension.class)
            .forMainSourceSet()
            .resources()
            .get();

        val generateResourcesDependencies = generateResources
            .getTaskDependencies()
            .getDependencies(generateResources)
            .stream()
            .map(Task.class::cast)
            .collect(toList());

        val processResources = project.getTasks().getByName("processResources");
        assertThat(generateResourcesDependencies)
            .doesNotContain(processResources);
    }

    @Test
    void generateGroovyDoesNotDependOnCompileGroovy() {
        project.getPluginManager().apply("groovy");
        val generateGroovy = project
            .getExtensions()
            .getByType(GenerateSourcesExtension.class)
            .forMainSourceSet()
            .groovy()
            .get();

        val generateGroovyDependencies = generateGroovy
            .getTaskDependencies()
            .getDependencies(generateGroovy)
            .stream()
            .map(Task.class::cast)
            .collect(toList());

        val compileGroovy = project.getTasks().getByName("compileGroovy");
        assertThat(generateGroovyDependencies)
            .doesNotContain(compileGroovy);
    }

    @Test
    void pluginTasksDoNotHavePropertyProblems() {
        project.getPluginManager().apply("groovy");
        project.getExtensions().getByType(GenerateSourcesExtension.class).forMainSourceSet(generate -> {
            generate.java();
            generate.resources();
            generate.groovy();
        });

        executeAfterEvaluateActions(project);

        val taskClassNamePrefix = packageNameOf(GenerateSourcesPlugin.class) + '.';
        project.getTasks().stream()
            .filter(task -> {
                val taskClass = unwrapGeneratedSubclass(task.getClass());
                return taskClass.getName().startsWith(taskClassNamePrefix);
            })
            .map(TaskValidations::markTaskDependenciesAsSkipped)
            .forEach(TaskValidations::assertNoTaskPropertiesProblems);
    }

}
