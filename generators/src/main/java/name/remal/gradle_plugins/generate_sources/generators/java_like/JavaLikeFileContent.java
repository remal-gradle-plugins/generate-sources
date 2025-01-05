package name.remal.gradle_plugins.generate_sources.generators.java_like;

import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeImportsChunk;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikePackageChunk;
import name.remal.gradle_plugins.generate_sources.generators.java_like.chunks.JavaLikeStaticImportsChunk;

public interface JavaLikeFileContent<Block extends JavaLikeContent<Block>>
    extends JavaLikeContent<Block>, JavaLikeWithPackageName, JavaLikeWithImports, JavaLikeWithStaticImports {

    @Override
    default String getPackageName() {
        return getChunk(JavaLikePackageChunk.class).getPackageName();
    }

    @Override
    default void addImport(String importName) {
        getChunk(JavaLikeImportsChunk.class).addImport(importName);
    }

    @Override
    default void addStaticImport(String className, String memberName) {
        getChunk(JavaLikeStaticImportsChunk.class).addStaticImport(className, memberName);
    }

}
