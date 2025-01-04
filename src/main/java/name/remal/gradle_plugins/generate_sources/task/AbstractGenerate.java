package name.remal.gradle_plugins.generate_sources.task;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.synchronizedSet;
import static name.remal.gradle_plugins.build_time_constants.api.BuildTimeConstants.getClassName;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.isClassPresent;
import static org.gradle.api.tasks.PathSensitivity.RELATIVE;

import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.GeneratingOutputStream;
import name.remal.gradle_plugins.generate_sources.generators.GeneratingWriter;
import name.remal.gradle_plugins.toolkit.EditorConfig;
import name.remal.gradle_plugins.toolkit.TaskPropertiesUtils;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.tasks.IgnoreEmptyDirectories;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskInputFilePropertyBuilder;

public abstract class AbstractGenerate
    extends DefaultTask {

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    {
        getOutputDirectory().convention(getLayout().getBuildDirectory()
            .dir("generated/" + getName())
        );
        onlyIf(__ -> {
            doFirst("Delete output dir", new DeleteFileSystemLocationTaskAction(
                getOutputDirectory().get().getAsFile()
            ));
            return true;
        });
    }


    private final Set<String> relativePaths = synchronizedSet(new LinkedHashSet<>());

    public final void binaryFile(
        String relativePath,
        Action<GeneratingOutputStream> action
    ) {
        if (!relativePaths.add(relativePath)) {
            getLogger().warn("Redefining generation of {}", relativePath);
        }

        doLast(relativePath, new GenerateBinaryFileTaskAction(relativePath, action));
    }

    public void textFile(
        String relativePath,
        @Nullable Charset charset,
        Action<GeneratingWriter> action
    ) {
        binaryFile(
            relativePath,
            sneakyThrowsAction(out -> {
                val editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());
                val generatingPath = out.getGeneratingPath();

                Charset detectedCharset = charset;
                if (detectedCharset == null) {
                    detectedCharset = editorConfig.getCharsetFor(generatingPath);
                }
                if (detectedCharset == null) {
                    detectedCharset = UTF_8;
                }

                String detectedLineSeparator = editorConfig.getLineSeparatorFor(generatingPath);
                if (detectedLineSeparator == null) {
                    detectedLineSeparator = "\n";
                }

                try (val writer = new GeneratingWriter(out, detectedCharset, detectedLineSeparator)) {
                    action.execute(writer);
                }
            })
        );
    }

    public void textFile(
        String relativePath,
        String encoding,
        Action<GeneratingWriter> action
    ) {
        textFile(relativePath, Charset.forName(encoding), action);
    }

    public void textFile(
        String relativePath,
        Action<GeneratingWriter> action
    ) {
        textFile(relativePath, (Charset) null, action);
    }


    public final void withInputProperty(String propertyName, @Nullable Object value) {
        getInputs().property(propertyName, value)
            .optional(true);
    }

    public final void withInputFiles(String propertyName, Object... paths) {
        configureFileProperty(getInputs().files(paths).withPropertyName(propertyName));
    }

    public final void withInputFile(String propertyName, Object path) {
        configureFileProperty(getInputs().file(path).withPropertyName(propertyName));
    }

    public final void withInputDir(String propertyName, Object path) {
        configureFileProperty(getInputs().dir(path).withPropertyName(propertyName));
    }

    private static void configureFileProperty(TaskInputFilePropertyBuilder property) {
        property
            .optional(true)
            .skipWhenEmpty(false)
            .withPathSensitivity(RELATIVE);
        if (IGNORE_EMPTY_DIRECTORIES_PRESENT) {
            IgnoreEmptyDirectoriesCustomizer.customize(property);
        }
    }

    private static final boolean IGNORE_EMPTY_DIRECTORIES_PRESENT = isClassPresent(
        getClassName(IgnoreEmptyDirectories.class),
        TaskPropertiesUtils.class.getClassLoader()
    );

    private static class IgnoreEmptyDirectoriesCustomizer {
        public static void customize(TaskInputFilePropertyBuilder property) {
            property.ignoreEmptyDirectories(true);
        }
    }


    @Inject
    protected abstract ProjectLayout getLayout();

}
