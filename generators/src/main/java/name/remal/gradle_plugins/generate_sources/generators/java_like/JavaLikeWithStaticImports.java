package name.remal.gradle_plugins.generate_sources.generators.java_like;

public interface JavaLikeWithStaticImports {

    void addStaticImport(String importName, String memberName);

    default void addStaticImport(Class<?> clazz, String memberName) {
        addStaticImport(clazz.getCanonicalName(), memberName);
    }

}
