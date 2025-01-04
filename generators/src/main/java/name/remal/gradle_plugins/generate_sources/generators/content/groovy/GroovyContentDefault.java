package name.remal.gradle_plugins.generate_sources.generators.content.groovy;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeContent;

public class GroovyContentDefault
    extends AbstractJavaLikeContent<GroovyContent>
    implements GroovyContent {

    public GroovyContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public GroovyContentDefault() {
    }

    @Override
    protected GroovyContent newBlock() {
        return new GroovyContentDefault(indent, lineSeparator);
    }


}
