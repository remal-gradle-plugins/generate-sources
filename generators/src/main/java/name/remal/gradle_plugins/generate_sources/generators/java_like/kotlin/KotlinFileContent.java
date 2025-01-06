package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import name.remal.gradle_plugins.generate_sources.generators.java_like.JavaLikeFileContent;

public interface KotlinFileContent
    extends KotlinContent, JavaLikeFileContent<KotlinContent> {

    @Override
    default void addStaticImport(String className, String memberName) {
        addImport(className + '.' + memberName);
    }

}
