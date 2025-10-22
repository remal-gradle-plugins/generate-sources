package name.remal.gradle_plugins.generate_sources.task;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.nio.file.Files.isDirectory;
import static java.util.function.Predicate.not;
import static name.remal.gradle_plugins.toolkit.GradleVersionUtils.isCurrentGradleVersionLessThan;
import static name.remal.gradle_plugins.toolkit.PathUtils.deleteRecursively;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsFunction;
import static name.remal.gradle_plugins.toolkit.TaskUtils.markAsNotCompatibleWithConfigurationCache;
import static org.gradle.api.tasks.PathSensitivity.ABSOLUTE;

import java.lang.StackWalker.StackFrame;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import name.remal.gradle_plugins.toolkit.EditorConfig;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.ClasspathNormalizer;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.Nullable;

@CacheableTask
public abstract class AbstractGenerate extends DefaultTask {

    {
        if (isCurrentGradleVersionLessThan("8.0")) {
            markAsNotCompatibleWithConfigurationCache(
                this,
                "Lambda serialization is broken in Configuration Cache of Gradle before 8"
            );
        }
    }


    /**
     * The directory property that represents the directory to generate source files into.
     * The default value is {@code build/generated/<task name>}
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    {
        getOutputDirectory().convention(
            getLayout().getBuildDirectory().dir("generated/" + getName())
        );
    }


    @TaskAction
    protected final void deleteOutputDir() {
        getOutputDirectory().finalizeValue();
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
        if (getState().isConfigurable()) {
            var callingScriptLocations = getCallingScriptLocations();
            callingScriptLocations.forEach(location -> {
                getLogger().debug("Add calling script location as an input dir: {}", location);
                getInputs().dir(location)
                    .optional()
                    .withPathSensitivity(ABSOLUTE)
                    .withNormalizer(ClasspathNormalizer.class);
            });
        }

        var taskAction = new GenerateBinaryFileTaskAction(relativePath, action);
        if (getState().getExecuting()) {
            taskAction.execute(this);
        } else {
            doLast(taskAction);
        }
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
        binaryFile(fixedProvider(relativePath), action);
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
        var projectDir = getLayout().getProjectDirectory();
        binaryFile(relativePath, sneakyThrowsAction(out -> {
            var editorConfig = new EditorConfig(projectDir.getAsFile().toPath());
            var generatingPath = out.getGeneratingPath();

            var detectedCharset = encoding
                .map(name -> name.isEmpty() ? null : Charset.forName(name))
                .getOrNull();
            if (detectedCharset == null) {
                detectedCharset = editorConfig.getCharsetFor(generatingPath);
            }

            var detectedLineSeparator = editorConfig.getLineSeparatorFor(generatingPath);

            try (var writer = new GeneratingWriter(out, detectedCharset, detectedLineSeparator)) {
                action.execute(writer);
            }
        }));
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
        textFile(fixedProvider(relativePath), fixedProvider(encoding), action);
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
        textFile(relativePath, fixedProvider(null), action);
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
        textFile(fixedProvider(relativePath), action);
    }


    private static Set<Path> getCallingScriptLocations() {
        var directories = new LinkedHashSet<Path>();
        return StackWalker.getInstance(RETAIN_CLASS_REFERENCE).walk(stream -> stream
            .skip(1)
            .map(StackFrame::getDeclaringClass)
            .filter(not(AbstractGenerate.class::isAssignableFrom))
            .takeWhile(clazz -> !Objects.equals(clazz.getClassLoader(), Object.class.getClassLoader()))
            .map(Class::getProtectionDomain)
            .filter(Objects::nonNull)
            .map(ProtectionDomain::getCodeSource)
            .filter(Objects::nonNull)
            .map(CodeSource::getLocation)
            .filter(Objects::nonNull)
            .filter(url -> url.toString().startsWith("file:"))
            .distinct()
            .map(sneakyThrowsFunction(URL::toURI))
            .map(Paths::get)
            .takeWhile(path -> {
                if (isDirectory(path)) {
                    directories.add(path);
                    return true;
                } else {
                    return directories.isEmpty();
                }
            })
            .filter(directories::contains)
            .collect(toImmutableSet())
        );
    }


    protected final <T> Provider<T> fixedProvider(@Nullable T value) {
        return getProviders().provider(() -> value);
    }

    @Inject
    protected abstract ProjectLayout getLayout();

    @Inject
    protected abstract ProviderFactory getProviders();

    @Inject
    protected abstract ObjectFactory getObjects();

}
