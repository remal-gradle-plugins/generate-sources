package name.remal.gradle_plugins.generate_sources.generators.java_like;

import name.remal.gradle_plugins.generate_sources.generators.ScriptContent;
import org.jspecify.annotations.Nullable;

public interface JavaLikeContent<Block extends JavaLikeContent<Block>>
    extends ScriptContent<Block> {

    void suppressWarningsLine(@Nullable String... warnings);

}
