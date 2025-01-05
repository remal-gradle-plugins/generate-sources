package name.remal.gradle_plugins.generate_sources.generators.java_like.chunks;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@SuppressWarnings("java:S1170")
public class JavaLikePackageChunkDefault extends AbstractJavaLikeChunk {

    @Getter
    private final String packageName;

    @Default
    private final String packageKeyword = "package";

    @Override
    public String toString() {
        if (packageName.isEmpty()) {
            return "";
        }

        return packageKeyword
            + ' '
            + packageName
            + statementDelimiter
            + lineSeparator
            + lineSeparator;
    }

}
