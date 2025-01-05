package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import javax.annotation.Nullable;
import lombok.Getter;

public class GroovyClassFileContentDefault
    extends GroovyFileContentDefault
    implements GroovyClassFileContent {

    @Getter
    private final String simpleName;

    public GroovyClassFileContentDefault(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
        this.simpleName = simpleName;
    }

    public GroovyClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
