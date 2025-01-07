package name.remal.gradle_plugins.generate_sources.generators;

import static java.util.stream.Collectors.joining;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

import com.google.common.base.Splitter;
import com.google.errorprone.annotations.ForOverride;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.val;
import org.gradle.api.Action;

public abstract class AbstractScriptContent<Block extends ScriptContent<Block>>
    extends TextContentDefault
    implements ScriptContent<Block> {

    @ForOverride
    protected abstract String getDefaultIndent();

    @ForOverride
    protected abstract String getBlockStart();

    @ForOverride
    protected abstract String getBlockEnd();


    protected final String indent;

    protected AbstractScriptContent(@Nullable String indent, @Nullable String lineSeparator) {
        super(lineSeparator);
        this.indent = isNotEmpty(indent) ? indent : getDefaultIndent();
    }

    protected AbstractScriptContent() {
        this(null, null);
    }

    @Override
    public void indent(Action<Block> action) {
        addLastChunks(indentImpl(action));
    }

    private static final Pattern NEW_LINE = Pattern.compile("(\\r\\n)|(\\n\\r)|(\\r)|(\\n)");

    private String indentImpl(Action<Block> action) {
        return withoutChunksAdding(() -> {
            val block = newBlock();
            action.execute(block);

            String blockContent = block.toString();
            if (blockContent.isEmpty()) {
                return "";
            }

            return Splitter.on(NEW_LINE).splitToStream(blockContent)
                .map(line -> {
                    if (!line.isEmpty()) {
                        return indent + line;
                    } else {
                        return line;
                    }
                })
                .collect(joining(lineSeparator));
        });
    }

    @Override
    public void block(CharSequence statement, Action<Block> action) {
        addLastChunks(blockImpl(statement, action));
    }

    private String blockImpl(CharSequence string, Action<Block> action) {
        return withoutChunksAdding(() -> {
            val content = new StringBuilder();

            val blockStart = getBlockStart();
            if (blockStart.isEmpty()) {
                if (string.length() > 0) {
                    content.append(string).append(lineSeparator);
                }
            } else {
                if (string.length() == 0) {
                    content.append(blockStart.trim()).append(lineSeparator);
                } else {
                    content.append(string).append(blockStart).append(lineSeparator);
                }
            }

            content.append(indentImpl(action));

            val blockEnd = getBlockEnd();
            if (!blockEnd.isEmpty()) {
                content.append(getBlockEnd()).append(lineSeparator);
            }

            return content.toString();
        });
    }

}
