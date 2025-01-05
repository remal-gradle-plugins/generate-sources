package name.remal.gradle_plugins.generate_sources.generators.java_like;

public interface JavaLikeWithImports {

    void addImport(String importName);

    default void addImport(Class<?> clazz) {
        addImport(clazz.getCanonicalName());
    }

}
