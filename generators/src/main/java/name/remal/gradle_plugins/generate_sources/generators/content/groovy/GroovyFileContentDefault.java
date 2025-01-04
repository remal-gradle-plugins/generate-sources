package name.remal.gradle_plugins.generate_sources.generators.content.groovy;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeFileContent;

public class GroovyFileContentDefault
    extends AbstractJavaLikeFileContent<GroovyContent>
    implements GroovyFileContent {

    public GroovyFileContentDefault(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
    }

    public GroovyFileContentDefault(@Nullable String packageName) {
        super(packageName);
    }

    @Override
    protected GroovyContent newBlock() {
        return new GroovyContentDefault(indent, lineSeparator);
    }

    @Override
    protected String getStatementDelimiter() {
        return "";
    }

}
