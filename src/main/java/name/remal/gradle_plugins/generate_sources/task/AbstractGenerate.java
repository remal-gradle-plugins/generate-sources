package name.remal.gradle_plugins.generate_sources.task;

import static name.remal.gradle_plugins.build_time_constants.api.BuildTimeConstants.getClassName;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.isClassPresent;
import static org.gradle.api.tasks.PathSensitivity.RELATIVE;

import java.nio.charset.Charset;
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
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
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


    public final void binaryFile(
        Provider<? extends CharSequence> relativePath,
        Action<? super GeneratingOutputStream> action
    ) {
        doLast(
            GenerateBinaryFileTaskAction.class.getName(),
            new GenerateBinaryFileTaskAction(relativePath, action)
        );
    }

    public final void binaryFile(
        CharSequence relativePath,
        Action<? super GeneratingOutputStream> action
    ) {
        binaryFile(provider(relativePath), action);
    }


    public void textFile(
        Provider<? extends CharSequence> relativePath,
        Provider<? extends CharSequence> encoding,
        Action<? super GeneratingWriter> action
    ) {
        binaryFile(
            relativePath,
            sneakyThrowsAction(out -> {
                val editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());
                val generatingPath = out.getGeneratingPath();

                Charset detectedCharset = encoding
                    .map(CharSequence::toString)
                    .map(name -> name.isEmpty() ? null : Charset.forName(name))
                    .getOrNull();
                if (detectedCharset == null) {
                    detectedCharset = editorConfig.getCharsetFor(generatingPath);
                }

                val detectedLineSeparator = editorConfig.getLineSeparatorFor(generatingPath);

                try (val writer = new GeneratingWriter(out, detectedCharset, detectedLineSeparator)) {
                    action.execute(writer);
                }
            })
        );
    }

    public void textFile(
        CharSequence relativePath,
        @Nullable CharSequence encoding,
        Action<? super GeneratingWriter> action
    ) {
        textFile(provider(relativePath), provider(null), action);
    }

    public void textFile(
        CharSequence relativePath,
        @Nullable Charset charset,
        Action<? super GeneratingWriter> action
    ) {
        textFile(relativePath, charset != null ? charset.name() : null, action);
    }

    public void textFile(
        Provider<? extends CharSequence> relativePath,
        Action<? super GeneratingWriter> action
    ) {
        textFile(relativePath, provider(null), action);
    }

    public void textFile(
        CharSequence relativePath,
        Action<? super GeneratingWriter> action
    ) {
        textFile(provider(relativePath), action);
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


    protected final <T> Provider<T> provider(@Nullable T value) {
        return getProviders().provider(() -> value);
    }

    @Inject
    protected abstract ProjectLayout getLayout();

    @Inject
    protected abstract ProviderFactory getProviders();

    @Inject
    protected abstract ObjectFactory getObjects();

}
