package name.remal.gradle_plugins.generate_sources.generators.content;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

import com.google.common.base.Splitter;
import com.google.errorprone.annotations.ForOverride;
import javax.annotation.Nullable;
import lombok.val;
import org.gradle.api.Action;

public abstract class AbstractJavaLikeContent<Block extends JavaLikeContent<Block>>
    extends TextContentDefault
    implements JavaLikeContent<Block> {

    @ForOverride
    protected abstract Block newBlock();


    protected static final String DEFAULT_INDENT = "    ";


    protected final String indent;

    protected AbstractJavaLikeContent(@Nullable String indent, @Nullable String lineSeparator) {
        super(lineSeparator);
        this.indent = isNotEmpty(indent) ? indent : DEFAULT_INDENT;
    }

    protected AbstractJavaLikeContent() {
        this.indent = DEFAULT_INDENT;
    }


    @Override
    public void indent(Action<Block> action) {
        val block = newBlock();
        action.execute(block);

        val blockContent = block.toString();
        if (blockContent.isEmpty()) {
            return;
        }

        val content = new StringBuilder();
        for (val line : Splitter.on(lineSeparator).split(blockContent)) {
            content.append(indent).append(line).append(lineSeparator);
        }
        chunks.add(content);
    }

    @Override
    public void block(CharSequence string, Action<Block> action) {
        if (string.length() == 0) {
            chunks.add('{' + lineSeparator);
        } else {
            chunks.add(string + " {" + lineSeparator);
        }
        indent(action);
        chunks.add('}' + lineSeparator);
    }

}
