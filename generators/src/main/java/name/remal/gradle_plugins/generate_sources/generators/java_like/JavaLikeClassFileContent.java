package name.remal.gradle_plugins.generate_sources.generators.java_like;

import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunk;

public interface JavaLikeClassFileContent<Block extends JavaLikeContent<Block>>
    extends JavaLikeFileContent<Block>, JavaLikeWithPackageName {

    /**
     * Get the package name of the generated file.
     */
    @Override
    default String getPackageName() {
        return getChunk(JavaLikePackageChunk.class).getPackageName();
    }

    /**
     * Get the class simple name of the generated file.
     */
    String getSimpleName();

}
