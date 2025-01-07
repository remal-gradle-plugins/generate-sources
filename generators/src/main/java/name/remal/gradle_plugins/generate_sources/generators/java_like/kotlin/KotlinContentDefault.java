package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.AbstractJavaLikeContent;
import org.gradle.api.Action;

public class KotlinContentDefault
    extends AbstractJavaLikeContent<KotlinContent>
    implements KotlinContent {

    public KotlinContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public KotlinContentDefault() {
        this(null, null);
    }

    @Override
    public KotlinContent newBlock() {
        return new KotlinContentDefault(indent, lineSeparator);
    }

    @Override
    public void block(CharSequence statement, Action<KotlinContent> action) {
        if (isBlank(statement)) {
            statement = "run";
        }
        super.block(statement, action);
    }

}
