package name.remal.gradle_plugins.generate_sources.generators.content;

public interface JavaLikeClassFileContent<Block extends JavaLikeContent<Block>>
    extends JavaLikeFileContent<Block> {

    String getSimpleName();

}
