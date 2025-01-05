package name.remal.gradle_plugins.generate_sources.generators.java_like.chunks;

import static name.remal.gradle_plugins.generate_sources.generators.TextContentDefault.DEFAULT_LINE_SEPARATOR;

import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import name.remal.gradle_plugins.generate_sources.generators.TextContentChunk;

@SuperBuilder
@SuppressWarnings("java:S1170")
public abstract class AbstractJavaLikeChunk implements TextContentChunk {

    @Override
    public abstract String toString();


    @Default
    protected final String statementDelimiter = ";";

    @Default
    protected final String lineSeparator = DEFAULT_LINE_SEPARATOR;

}
