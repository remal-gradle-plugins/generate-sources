package name.remal.gradle_plugins.generate_sources.task;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static name.remal.gradle_plugins.build_time_constants.api.BuildTimeConstants.getClassName;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;
import static name.remal.gradle_plugins.toolkit.PathUtils.deleteRecursively;
import static name.remal.gradle_plugins.toolkit.PredicateUtils.not;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsFunction;
import static name.remal.gradle_plugins.toolkit.reflection.ReflectionUtils.isClassPresent;
import static name.remal.gradle_plugins.toolkit.reflection.WhoCalledUtils.getCallingClasses;
import static org.gradle.api.tasks.PathSensitivity.RELATIVE;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.inject.Inject;
import name.remal.gradle_plugins.toolkit.EditorConfig;
import name.remal.gradle_plugins.toolkit.TaskPropertiesUtils;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.ClasspathNormalizer;
import org.gradle.api.tasks.IgnoreEmptyDirectories;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskInputFilePropertyBuilder;
import org.gradle.api.tasks.TaskInputPropertyBuilder;
import org.gradle.api.tasks.TaskInputs;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;

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
        var callingLocationDirs = getCallingLocationDirs();
        if (isNotEmpty(callingLocationDirs)) {
            configureFileProperty(
                getInputs().files(callingLocationDirs)
            )
                .withNormalizer(ClasspathNormalizer.class);
        }

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
        binaryFile(fixedProvider(relativePath), action);
    }

    private static List<Path> getCallingLocationDirs() {
        return getCallingClasses(2).stream()
            .filter(not(AbstractGenerate.class::isAssignableFrom))
            .map(Class::getProtectionDomain)
            .filter(Objects::nonNull)
            .map(ProtectionDomain::getCodeSource)
            .filter(Objects::nonNull)
            .map(CodeSource::getLocation)
            .filter(Objects::nonNull)
            .filter(url -> url.getProtocol().equals("file"))
            .map(sneakyThrowsFunction(URL::toURI))
            .distinct()
            .map(Paths::get)
            .filter(Files::isDirectory)
            .collect(toList());
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
                var editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());
                var generatingPath = out.getGeneratingPath();

                Charset detectedCharset = encoding
                    .map(name -> name.isEmpty() ? null : Charset.forName(name))
                    .getOrNull();
                if (detectedCharset == null) {
                    detectedCharset = editorConfig.getCharsetFor(generatingPath);
                }

                var detectedLineSeparator = editorConfig.getLineSeparatorFor(generatingPath);

                try (var writer = new GeneratingWriter(out, detectedCharset, detectedLineSeparator)) {
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


    /**
     * Register an optional input property.
     * See {@link TaskInputs#property(String, Object)}.
     */
    public final TaskInputPropertyBuilder withInputProperty(String propertyName, @Nullable Object value) {
        return getInputs().property(propertyName, value)
            .optional(true);
    }

    /**
     * Register optional input files.
     * See {@link TaskInputs#files(Object...)}.
     *
     * <p>If any path is a {@link Class},
     * {@link Class#getProtectionDomain()
     * Class.protectionDomain
     * }.{@link ProtectionDomain#getCodeSource() codeSource}.{@link CodeSource#getLocation() location} will be used.
     */
    public final TaskInputFilePropertyBuilder withInputFiles(String propertyName, Object... paths) {
        paths = stream(paths)
            .map(this::processPropertyFile)
            .filter(Objects::nonNull)
            .toArray(Object[]::new);

        return configureFileProperty(
            getInputs().files(paths).withPropertyName(propertyName)
        );
    }

    /**
     * Register optional input classpath files.
     * See {@link TaskInputs#files(Object...)}.
     *
     * <p>If any path is a {@link Class},
     * {@link Class#getProtectionDomain()
     * Class.protectionDomain
     * }.{@link ProtectionDomain#getCodeSource() codeSource}.{@link CodeSource#getLocation() location} will be used.
     */
    public final TaskInputFilePropertyBuilder withInputClasspathFiles(String propertyName, Object... paths) {
        return withInputFiles(propertyName, paths)
            .withNormalizer(ClasspathNormalizer.class);
    }

    /**
     * Register an optional input file.
     * See {@link TaskInputs#file(Object)}.
     *
     * <p>If the provided path is a {@link Class},
     * {@link Class#getProtectionDomain()
     * Class.protectionDomain
     * }.{@link ProtectionDomain#getCodeSource() codeSource}.{@link CodeSource#getLocation() location} will be used.
     */
    public final TaskInputFilePropertyBuilder withInputFile(String propertyName, Object path) {
        path = processPropertyFile(path);
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }

        return configureFileProperty(
            getInputs().file(path).withPropertyName(propertyName)
        );
    }

    /**
     * Register an optional input directory.
     * See {@link TaskInputs#dir(Object)}.
     *
     * <p>If the provided path is a {@link Class},
     * {@link Class#getProtectionDomain()
     * Class.protectionDomain
     * }.{@link ProtectionDomain#getCodeSource() codeSource}.{@link CodeSource#getLocation() location} will be used.
     */
    public final TaskInputFilePropertyBuilder withInputDir(String propertyName, Object path) {
        path = processPropertyFile(path);
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }

        return configureFileProperty(
            getInputs().dir(path).withPropertyName(propertyName)
        );
    }

    @Nullable
    @Contract("null->null")
    private Object processPropertyFile(@Nullable Object path) {
        if (path == null) {
            return null;
        }

        if (path instanceof Class) {
            var uri = Optional.ofNullable(((Class<?>) path).getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(sneakyThrowsFunction(URL::toURI))
                .orElse(null);
            if (uri == null) {
                return null;
            }

            var scheme = uri.getScheme();
            if (scheme.equals("file")) {
                return uri;
            }

            return null;
        }

        if (path instanceof Provider) {
            return ((Provider<?>) path).map(this::processPropertyFile);
        }

        if (path instanceof Iterable) {
            return getProviders().provider(() ->
                StreamSupport.stream(((Iterable<?>) path).spliterator(), false)
                    .map(this::processPropertyFile)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toList())
            );
        }

        return path;
    }

    private static TaskInputFilePropertyBuilder configureFileProperty(TaskInputFilePropertyBuilder property) {
        property = property
            .optional(true)
            .skipWhenEmpty(false)
            .withPathSensitivity(RELATIVE);
        if (IGNORE_EMPTY_DIRECTORIES_PRESENT) {
            property = IgnoreEmptyDirectoriesCustomizer.customize(property);
        }
        return property;
    }

    private static final boolean IGNORE_EMPTY_DIRECTORIES_PRESENT = isClassPresent(
        getClassName(IgnoreEmptyDirectories.class),
        TaskPropertiesUtils.class.getClassLoader()
    );

    private static class IgnoreEmptyDirectoriesCustomizer {
        public static TaskInputFilePropertyBuilder customize(TaskInputFilePropertyBuilder property) {
            return property.ignoreEmptyDirectories(true);
        }
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
