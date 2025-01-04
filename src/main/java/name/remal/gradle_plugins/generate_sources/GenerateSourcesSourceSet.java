package name.remal.gradle_plugins.generate_sources;

import static java.lang.String.format;
import static lombok.AccessLevel.PUBLIC;

import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.task.AbstractGenerate;
import name.remal.gradle_plugins.generate_sources.task.GenerateGroovy;
import name.remal.gradle_plugins.generate_sources.task.GenerateJava;
import name.remal.gradle_plugins.generate_sources.task.GenerateResources;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.GroovySourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.GroovyCompile;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.language.jvm.tasks.ProcessResources;

@Getter
@RequiredArgsConstructor(access = PUBLIC, onConstructor_ = {@Inject})
public abstract class GenerateSourcesSourceSet {

    private final SourceSet sourceSet;


    public TaskProvider<GenerateJava> java() {
        return createIfNeeded(
            "java",
            "Java sources",
            GenerateJava.class,
            SourceSet::getJava,
            SourceSet::getCompileJavaTaskName,
            JavaCompile.class,
            (generate, compileProvider) -> {
                generate.getEncoding().convention(compileProvider
                    .map(JavaCompile::getOptions)
                    .map(CompileOptions::getEncoding)
                );
            }
        );
    }

    public TaskProvider<GenerateJava> java(Action<GenerateJava> action) {
        val generateProvider = java();
        generateProvider.configure(action);
        return generateProvider;
    }


    public TaskProvider<GenerateResources> resources() {
        return createIfNeeded(
            "resources",
            "resources",
            GenerateResources.class,
            SourceSet::getResources,
            SourceSet::getProcessResourcesTaskName,
            ProcessResources.class,
            (generate, processTaskProvider) -> {
            }
        );
    }

    public TaskProvider<GenerateResources> resources(Action<GenerateResources> action) {
        val generateProvider = resources();
        generateProvider.configure(action);
        return generateProvider;
    }


    public TaskProvider<GenerateGroovy> groovy() {
        return createIfNeeded(
            "groovy",
            "Groovy sources",
            GenerateGroovy.class,
            sourceSet -> sourceSet.getExtensions().getByType(GroovySourceDirectorySet.class),
            sourceSet -> sourceSet.getCompileTaskName("groovy"),
            GroovyCompile.class,
            (generate, compileProvider) -> {
                generate.getEncoding().convention(compileProvider
                    .map(GroovyCompile::getOptions)
                    .map(CompileOptions::getEncoding)
                );
            }
        );
    }

    public TaskProvider<GenerateGroovy> groovy(Action<GenerateGroovy> action) {
        val generateProvider = groovy();
        generateProvider.configure(action);
        return generateProvider;
    }


    @SuppressWarnings("java:S107")
    private <
        Generate extends AbstractGenerate<Generate>,
        ProcessTask extends Task
        > TaskProvider<Generate> createIfNeeded(
        String target,
        String targetDescription,
        Class<Generate> generateTaskType,
        Function<? super SourceSet, ? extends SourceDirectorySet> sourceSetSourcesGetter,
        Function<? super SourceSet, String> processTaskNameGetter,
        Class<ProcessTask> compileTaskType,
        BiConsumer<? super Generate, TaskProvider<? extends ProcessTask>> configureGenerate
    ) {
        val generateTaskName = sourceSet.getTaskName("generate", target);

        final TaskProvider<Generate> generateProvider;
        if (getTasks().getNames().contains(generateTaskName)) {
            generateProvider = getTasks().named(generateTaskName, generateTaskType);
        } else {
            val processTaskName = processTaskNameGetter.apply(sourceSet);
            val processTaskProvider = getTasks().named(processTaskName, compileTaskType);

            generateProvider = getTasks().register(generateTaskName, generateTaskType, generate -> {
                generate.setDescription(format(
                    "Generate %s for %s",
                    targetDescription,
                    sourceSet
                ));

                configureGenerate.accept(generate, processTaskProvider);
            });

            val outputDir = getObjects().fileCollection()
                .builtBy(generateProvider)
                .from(generateProvider.map(AbstractGenerate::getOutputDirectory));
            sourceSetSourcesGetter.apply(sourceSet).srcDir(outputDir);

            processTaskProvider.configure(processTask -> processTask.dependsOn(generateProvider));
        }

        return generateProvider;
    }


    @Inject
    protected abstract TaskContainer getTasks();

    @Inject
    protected abstract ObjectFactory getObjects();

}
