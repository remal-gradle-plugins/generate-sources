package name.remal.gradle_plugins.generate_sources;

import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.packageNameOf;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.unwrapGeneratedSubclass;
import static name.remal.gradle_plugins.toolkit.testkit.ProjectValidations.executeAfterEvaluateActions;

import lombok.RequiredArgsConstructor;
import lombok.val;
import name.remal.gradle_plugins.toolkit.testkit.TaskValidations;
import org.gradle.api.Project;
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
    void pluginTasksDoNotHavePropertyProblems() {
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
