package name.remal.gradle_plugins.generate_sources.generators.java_like.chunks;

import java.util.Set;
import java.util.TreeSet;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import lombok.val;

@SuperBuilder
@SuppressWarnings("java:S1170")
public class JavaLikeStaticImportsChunkDefault extends AbstractJavaLikeChunk implements JavaLikeStaticImportsChunk {

    private final Set<String> staticImports = new TreeSet<>();

    @Default
    private final String staticImportKeyword = "import static";

    @Override
    public String toString() {
        if (staticImports.isEmpty()) {
            return "";
        }

        val content = new StringBuilder();
        for (val importName : staticImports) {
            content
                .append(staticImportKeyword)
                .append(' ')
                .append(importName)
                .append(statementDelimiter)
                .append(lineSeparator);
        }
        content.append(lineSeparator);
        return content.toString();
    }

    @Override
    public void addStaticImport(String importName, String memberName) {
        staticImports.add(importName + '.' + memberName);
    }

}
