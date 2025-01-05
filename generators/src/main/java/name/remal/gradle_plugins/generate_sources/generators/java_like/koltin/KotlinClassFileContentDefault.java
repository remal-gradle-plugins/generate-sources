package name.remal.gradle_plugins.generate_sources.generators.java_like.koltin;

import javax.annotation.Nullable;
import lombok.Getter;

public class KotlinClassFileContentDefault
    extends KotlinFileContentDefault
    implements KotlinClassFileContent {

    @Getter
    private final String simpleName;

    public KotlinClassFileContentDefault(
        @Nullable String packageName,
        String simpleName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(packageName, indent, lineSeparator);
        this.simpleName = simpleName;
    }

    public KotlinClassFileContentDefault(@Nullable String packageName, String simpleName) {
        this(packageName, simpleName, null, null);
    }

}
