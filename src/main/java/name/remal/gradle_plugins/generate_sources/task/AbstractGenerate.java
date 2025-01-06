package name.remal.gradle_plugins.generate_sources.task;

import static name.remal.gradle_plugins.build_time_constants.api.BuildTimeConstants.getClassName;
import static name.remal.gradle_plugins.toolkit.PathUtils.deleteRecursively;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.isClassPresent;
import static org.gradle.api.tasks.PathSensitivity.RELATIVE;

import java.nio.charset.Charset;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.val;
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
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskInputFilePropertyBuilder;
import org.gradle.api.tasks.TaskInputs;
import org.intellij.lang.annotations.Language;

public abstract class AbstractGenerate
    extends DefaultTask {

    /**
     * The directory property that represents the directory to generate source files into.
     * The default value is {@code build/generated/<task name>}
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    {
        getOutputDirectory().convention(getLayout().getBuildDirectory()
            .dir("generated/" + getName())
        );
    }


    @TaskAction
    protected final void deleteOutputDir() {
        deleteRecursively(getOutputDirectory().get().getAsFile().toPath());
    }


    /**
     * Configure a binary file generation.
     *
     * @param relativePath The relative path of the binary file.
     * @param action Generation logic.
     */
    public final void binaryFile(
        Provider<String> relativePath,
        Action<? super GeneratingOutputStream> action
    ) {
        doLast(new GenerateBinaryFileTaskAction(relativePath, action));
    }

    /**
     * Configure a binary file generation.
     *
     * @param relativePath The relative path of the binary file.
     * @param action Generation logic.
     */
    public final void binaryFile(
        String relativePath,
        Action<? super GeneratingOutputStream> action
    ) {
        binaryFile(provider(relativePath), action);
    }


    /**
     * Configure a text file generation.
     *
     * <p>If encoding is not passed, it will be read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code UTF-8} wil lbe used.
     *
     * <p>The line separator is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code \n} wil lbe used.
     *
     * @param relativePath The relative path of the text file.
     * @param encoding The encoding of the text file. Optional.
     * @param action Generation logic.
     */
    public void textFile(
        Provider<String> relativePath,
        Provider<String> encoding,
        Action<? super GeneratingWriter> action
    ) {
        binaryFile(
            relativePath,
            sneakyThrowsAction(out -> {
                val editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());
                val generatingPath = out.getGeneratingPath();

                Charset detectedCharset = encoding
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

    /**
     * Configure a text file generation.
     *
     * <p>If encoding is not passed, it will be read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code UTF-8} wil lbe used.
     *
     * <p>The line separator is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code \n} wil lbe used.
     *
     * @param relativePath The relative path of the text file.
     * @param encoding The encoding of the text file. Optional.
     * @param action Generation logic.
     */
    public void textFile(
        String relativePath,
        @Nullable @Language("encoding-reference") String encoding,
        Action<? super GeneratingWriter> action
    ) {
        textFile(provider(relativePath), provider(encoding), action);
    }

    /**
     * Configure a text file generation.
     *
     * <p>If charset is not passed, it will be read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code UTF-8} wil lbe used.
     *
     * <p>The line separator is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code \n} wil lbe used.
     *
     * @param relativePath The relative path of the text file.
     * @param charset The charset of the text file. Optional.
     * @param action Generation logic.
     */
    public void textFile(
        String relativePath,
        @Nullable Charset charset,
        Action<? super GeneratingWriter> action
    ) {
        textFile(relativePath, charset != null ? charset.name() : null, action);
    }

    /**
     * Configure a text file generation.
     *
     * <p>The encoding is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code UTF-8} wil lbe used.
     *
     * <p>The line separator is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code \n} wil lbe used.
     *
     * @param relativePath The relative path of the text file.
     * @param action Generation logic.
     */
    public void textFile(
        Provider<String> relativePath,
        Action<? super GeneratingWriter> action
    ) {
        textFile(relativePath, provider(null), action);
    }

    /**
     * Configure a text file generation.
     *
     * <p>The encoding is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code UTF-8} wil lbe used.
     *
     * <p>The line separator is read from {@code .editorconfig} file.
     * If {@code .editorconfig} file can't be found, {@code \n} wil lbe used.
     *
     * @param relativePath The relative path of the text file.
     * @param action Generation logic.
     */
    public void textFile(
        String relativePath,
        Action<? super GeneratingWriter> action
    ) {
        textFile(provider(relativePath), action);
    }


    /**
     * Register an optional input property.
     * See {@link TaskInputs#property(String, Object)}.
     */
    public final void withInputProperty(String propertyName, @Nullable Object value) {
        getInputs().property(propertyName, value)
            .optional(true);
    }

    /**
     * Register optional input files.
     * See {@link TaskInputs#files(Object...)}.
     */
    public final void withInputFiles(String propertyName, Object... paths) {
        configureFileProperty(getInputs().files(paths).withPropertyName(propertyName));
    }

    /**
     * Register an optional input file.
     * See {@link TaskInputs#file(Object)}.
     */
    public final void withInputFile(String propertyName, Object path) {
        configureFileProperty(getInputs().file(path).withPropertyName(propertyName));
    }

    /**
     * Register an optional input directory.
     * See {@link TaskInputs#dir(Object)}.
     */
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
