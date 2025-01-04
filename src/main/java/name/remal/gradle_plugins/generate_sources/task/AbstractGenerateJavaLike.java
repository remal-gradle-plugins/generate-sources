package name.remal.gradle_plugins.generate_sources.task;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;
import static name.remal.gradle_plugins.toolkit.SneakyThrowUtils.sneakyThrowsAction;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.google.errorprone.annotations.ForOverride;
import java.nio.file.Path;
import javax.annotation.Nullable;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.content.JavaLikeClassFileContent;
import name.remal.gradle_plugins.generate_sources.generators.content.JavaLikeContent;
import name.remal.gradle_plugins.generate_sources.generators.content.JavaLikeFileContent;
import name.remal.gradle_plugins.toolkit.EditorConfig;
import org.gradle.api.Action;
import org.gradle.api.provider.Provider;

public abstract class AbstractGenerateJavaLike<
    Block extends JavaLikeContent<Block>,
    FileContent extends JavaLikeFileContent<Block>,
    ClassFileContent extends JavaLikeClassFileContent<Block>
    >
    extends AbstractGenerateText {

    @ForOverride
    protected abstract FileContent createFileContent(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    );

    @ForOverride
    protected abstract String getClassFileExtension();

    @ForOverride
    protected abstract ClassFileContent createClassFileContent(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    );


    public final void scriptFile(
        Provider<? extends CharSequence> relativePath,
        Provider<? extends CharSequence> packageName,
        Action<? super FileContent> action
    ) {
        textFile(relativePath, sneakyThrowsAction(writer -> {
            val indent = getIndent(writer.getGeneratingPath());
            val lineSeparator = writer.getLineSeparator();
            val content = createFileContent(
                packageName.map(CharSequence::toString).getOrNull(),
                indent,
                lineSeparator
            );
            action.execute(content);
            writer.write(content.toString());
        }));
    }

    public final void scriptFile(
        CharSequence relativePath,
        CharSequence packageName,
        Action<? super FileContent> action
    ) {
        scriptFile(provider(relativePath), provider(packageName), action);
    }

    public final void scriptFile(
        Provider<? extends CharSequence> relativePath,
        Action<? super FileContent> action
    ) {
        scriptFile(relativePath, provider(null), action);
    }

    public final void scriptFile(
        CharSequence relativePath,
        Action<? super FileContent> action
    ) {
        scriptFile(provider(relativePath), action);
    }


    public final void classFile(
        Provider<? extends CharSequence> packageName,
        Provider<? extends CharSequence> simpleName,
        Action<? super ClassFileContent> action
    ) {
        val packageNameFinalized = getObjects().property(CharSequence.class).value(packageName);
        packageNameFinalized.finalizeValueOnRead();

        val simpleNameFinalized = getObjects().property(CharSequence.class).value(simpleName);
        simpleNameFinalized.finalizeValueOnRead();

        val relativePath = getProviders().provider(() -> {
            val result = new StringBuilder();
            val packageNameStr = packageNameFinalized.getOrNull();
            if (isNotEmpty(packageNameStr)) {
                result.append(packageNameStr.toString().replace('.', '/'));
                result.append('/');
            }
            result.append(simpleNameFinalized.get());
            val extension = getClassFileExtension();
            if (isNotEmpty(extension)) {
                result.append('.').append(extension);
            }
            return result;
        });

        textFile(relativePath, sneakyThrowsAction(writer -> {
            val indent = getIndent(writer.getGeneratingPath());
            val lineSeparator = writer.getLineSeparator();
            val content = createClassFileContent(
                packageNameFinalized.map(CharSequence::toString).getOrNull(),
                simpleNameFinalized.map(CharSequence::toString).get(),
                indent,
                lineSeparator
            );
            action.execute(content);
            writer.write(content.toString());
        }));
    }

    public final void classFile(
        @Nullable CharSequence packageName,
        CharSequence simpleName,
        Action<? super ClassFileContent> action
    ) {
        classFile(provider(packageName), provider(simpleName), action);
    }


    @Nullable
    private String getIndent(Path generatingPath) {
        val editorConfig = new EditorConfig(getLayout().getProjectDirectory().getAsFile().toPath());

        val indentStyle = editorConfig.getPropertiesFor(generatingPath).get("indent_style");
        if ("tab".equalsIgnoreCase(indentStyle)) {
            return "\t";

        } else {
            val indentSizeString = editorConfig.getPropertiesFor(generatingPath).get("indent_size");
            if (indentSizeString != null) {
                try {
                    val indentSize = max(1, parseInt(indentSizeString));
                    return repeat(' ', indentSize);

                } catch (NumberFormatException ignored) {
                    // do nothing
                }
            }
        }

        return null;
    }

}
