package name.remal.gradle_plugins.generate_sources.generators;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

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
    protected abstract Block newBlock();

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
        chunks.add(indentImpl(action));
    }

    private static final Pattern INDENT_NEXT_LINE = Pattern.compile("\\n([^\\n])");

    private String indentImpl(Action<Block> action) {
        val block = newBlock();
        action.execute(block);

        String blockContent = block.toString();
        if (blockContent.isEmpty()) {
            return "";
        }

        blockContent = indent + INDENT_NEXT_LINE.matcher(blockContent).replaceAll("\n" + indent + "$1");

        return blockContent;
    }

    @Override
    public void block(CharSequence string, Action<Block> action) {
        chunks.add(blockImpl(string, action));
    }

    private String blockImpl(CharSequence string, Action<Block> action) {
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
    }

}
