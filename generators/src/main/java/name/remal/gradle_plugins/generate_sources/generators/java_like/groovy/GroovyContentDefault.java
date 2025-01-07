package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.java_like.AbstractJavaLikeContent;

public class GroovyContentDefault
    extends AbstractJavaLikeContent<GroovyContent>
    implements GroovyContent {

    public GroovyContentDefault(@Nullable String indent, @Nullable String lineSeparator) {
        super(indent, lineSeparator);
    }

    public GroovyContentDefault() {
        this(null, null);
    }

    @Override
    public GroovyContent newBlock() {
        return new GroovyContentDefault(indent, lineSeparator);
    }

}
