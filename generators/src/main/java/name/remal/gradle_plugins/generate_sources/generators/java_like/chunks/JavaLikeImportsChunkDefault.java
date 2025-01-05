package name.remal.gradle_plugins.generate_sources.generators.java_like.chunks;

import java.util.Set;
import java.util.TreeSet;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import lombok.val;

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

        val content = new StringBuilder();
        for (val importName : imports) {
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
