package name.remal.gradle_plugins.generate_sources.generators.content.koltin;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeContent;

public class KotlinContentDefault
    extends AbstractJavaLikeContent<KotlinContent>
    implements KotlinContent {

    public KotlinContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public KotlinContentDefault() {
    }

    @Override
    protected KotlinContent newBlock() {
        return new KotlinContentDefault(indent, lineSeparator);
    }


}
