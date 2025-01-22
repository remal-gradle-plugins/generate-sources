package name.remal.gradle_plugins.generate_sources.generators.java_like.chunks;

import java.util.Set;
import java.util.TreeSet;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@SuppressWarnings("java:S1170")
public class JavaLikeImportsChunkDefault extends AbstractJavaLikeChunk implements JavaLikeImportsChunk {

    private final Set<String> imports = new TreeSet<>();

    @Default
    private final String importKeyword = "import";

    @Override
    public String toString() {
        if (imports.isEmpty()) {
            return "";
        }

        var content = new StringBuilder();
        for (var importName : imports) {
            content
                .append(importKeyword)
                .append(' ')
                .append(importName)
                .append(statementDelimiter)
                .append(lineSeparator);
        }
        content.append(lineSeparator);
        return content.toString();
    }

    @Override
    public void addImport(String importName) {
        imports.add(importName);
    }

}
