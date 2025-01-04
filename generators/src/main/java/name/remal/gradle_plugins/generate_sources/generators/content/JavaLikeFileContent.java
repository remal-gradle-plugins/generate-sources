package name.remal.gradle_plugins.generate_sources.generators.content;

public interface JavaLikeFileContent<Block extends JavaLikeContent<Block>>
    extends JavaLikeContent<Block> {

    String getPackageName();

    void addStaticImport(String className, String memberName);

    default void addStaticImport(Class<?> clazz, String memberName) {
        addStaticImport(clazz.getCanonicalName(), memberName);
    }

    void addImport(String importName);

    default void addImport(Class<?> clazz) {
        addImport(clazz.getCanonicalName());
    }

}
