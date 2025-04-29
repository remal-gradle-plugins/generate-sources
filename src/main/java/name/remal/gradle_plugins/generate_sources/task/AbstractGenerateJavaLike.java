package name.remal.gradle_plugins.generate_sources.task;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.google.errorprone.annotations.ForOverride;
import java.nio.file.Path;
import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeClassFileContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeContent;
import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeFileContent;
import name.remal.gradle_plugins.toolkit.EditorConfig;
import org.gradle.api.Action;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Internal;

@CacheableTask
public abstract class AbstractGenerateJavaLike<
    Block extends JavaLikeContent<Block>,
    FileContent extends JavaLikeFileContent<Block>,
    ClassFileContent extends JavaLikeClassFileContent<Block>
    >
    extends AbstractGenerateText {

    @ForOverride
    protected abstract FileContent createFileContent(
        @Nullable String indent,
        @Nullable String lineSeparator
    );

    @ForOverride
    @Internal
    protected abstract String getClassFileExtension();

    @ForOverride
    protected abstract ClassFileContent createClassFileContent(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    );


    /**
     * Configure a JVM-like class file generation.
     *
     * @param packageName The package name of the generated class. Optional.
     * @param simpleName The simple class name of the generated class.
     * @param action Generation logic.
     */
    public final void classFile(
        Provider<String> packageName,
        Provider<String> simpleName,
        Action<? super ClassFileContent> action
    ) {
        var packageNameFinalized = getObjects().property(String.class).value(packageName);
        packageNameFinalized.finalizeValueOnRead();

        var simpleNameFinalized = getObjects().property(String.class).value(simpleName);
        simpleNameFinalized.finalizeValueOnRead();

        var relativePath = getProviders().provider(() -> {
            var result = new StringBuilder();
            var packageNameStr = packageNameFinalized.getOrNull();
            if (isNotEmpty(packageNameStr)) {
                result.append(packageNameStr.replace('.', '/'));
                result.append('/');
            }
            result.append(simpleNameFinalized.get());
            var extension = getClassFileExtension();
            if (isNotEmpty(extension)) {
                result.append('.').append(extension);
            }
            return result.toString();
        });

        textFile(relativePath, sneakyThrowsAction(writer -> {
            var indent = getIndent(writer.getGeneratingPath());
            var lineSeparator = writer.getLineSeparator();
            var content = createClassFileContent(
                packageNameFinalized.getOrNull(),
                simpleNameFinalized.get(),
                indent,
                lineSeparator
            );
            action.execute(content);
            writer.write(content.toString());
        }));
    }

    /**
     * Configure a JVM-like class file generation.
     *
     * @param packageName The package name of the generated class. Optional.
     * @param simpleName The simple class name of the generated class.
     * @param action Generation logic.
     */
    public final void classFile(
        @Nullable String packageName,
        String simpleName,
        Action<? super ClassFileContent> action
    ) {
        classFile(fixedProvider(packageName), fixedProvider(simpleName), action);
    }


    /**
     * Configure a JVM-like script file generation.
     * A "script" is a file of the task's language without {@code package} statement.
     * Imports and static imports are still allowed.
     *
     * @param relativePath The relative path of the text file.
     * @param action Generation logic.
     */
    public final void scriptFile(
        Provider<String> relativePath,
        Action<? super FileContent> action
    ) {
        textFile(relativePath, sneakyThrowsAction(writer -> {
            var indent = getIndent(writer.getGeneratingPath());
            var lineSeparator = writer.getLineSeparator();
            var content = createFileContent(
                indent,
                lineSeparator
            );
            action.execute(content);
            writer.write(content.toString());
        }));
    }

    /**
     * Configure a JVM-like script file generation.
     * A "script" is a file of the task's language without {@code package} statement.
     * Imports and static imports are still allowed.
     *
     * @param relativePath The relative path of the text file.
     * @param action Generation logic.
     */
    public final void scriptFile(
        String relativePath,
        Action<? super FileContent> action
    ) {
        scriptFile(fixedProvider(relativePath), action);
    }


    @Nullable
    private String getIndent(Path generatingPath) {
        var editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());

        var indentStyle = editorConfig.getPropertiesFor(generatingPath).get("indent_style");
        if ("tab".equalsIgnoreCase(indentStyle)) {
            return "\t";

        } else {
            var indentSizeString = editorConfig.getPropertiesFor(generatingPath).get("indent_size");
            if (indentSizeString != null) {
                try {
                    var indentSize = max(1, parseInt(indentSizeString));
                    return repeat(' ', indentSize);

                } catch (NumberFormatException ignored) {
                    // do nothing
                }
            }
        }

        return null;
    }

}
