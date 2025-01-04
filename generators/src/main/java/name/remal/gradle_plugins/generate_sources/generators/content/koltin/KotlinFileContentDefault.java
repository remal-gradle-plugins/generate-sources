package name.remal.gradle_plugins.generate_sources.generators.content.koltin;

import javax.annotation.Nullable;
import name.remal.gradle_plugins.generate_sources.generators.content.AbstractJavaLikeFileContent;

public class KotlinFileContentDefault
    extends AbstractJavaLikeFileContent<KotlinContent>
    implements KotlinFileContent {

    public KotlinFileContentDefault(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
    }

    public KotlinFileContentDefault(@Nullable String packageName) {
        super(packageName);
    }

    @Override
    protected KotlinContent newBlock() {
        return new KotlinContentDefault(indent, lineSeparator);
    }

    @Override
    protected String getStatementDelimiter() {
        return "";
    }

    @Override
    protected String getStaticImportKeyword() {
        return "import";
    }

}
