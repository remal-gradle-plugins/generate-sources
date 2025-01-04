package name.remal.gradle_plugins.generate_sources.generators.content;

import static name.remal.gradle_plugins.toolkit.ObjectUtils.defaultValue;
import static name.remal.gradle_plugins.toolkit.ObjectUtils.isNotEmpty;

import com.google.errorprone.annotations.ForOverride;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.val;

public abstract class AbstractJavaLikeFileContent<Block extends JavaLikeContent<Block>>
    extends AbstractJavaLikeContent<Block>
    implements JavaLikeFileContent<Block> {

    @ForOverride
    protected String getStatementDelimiter() {
        return ";";
    }

    @ForOverride
    protected String getPackageKeyword() {
        return "package";
    }

    @ForOverride
    protected String getStaticImportKeyword() {
        return "import static";
    }

    @ForOverride
    protected String getImportKeyword() {
        return "import";
    }


    @Getter
    protected final String packageName;

    protected AbstractJavaLikeFileContent(
        @Nullable String packageName,
        @Nullable String indent,
        @Nullable String lineSeparator
    ) {
        super(indent, lineSeparator);
        this.packageName = defaultValue(packageName);
    }

    protected AbstractJavaLikeFileContent(@Nullable String packageName) {
        this(packageName, null, null);
    }


    {
        chunks.add(new DynamicCharSequence(() -> {
            val packageName = getPackageName();
            if (isNotEmpty(packageName)) {
                return getPackageKeyword()
                    + ' '
                    + packageName
                    + getStatementDelimiter()
                    + lineSeparator
                    + lineSeparator;
            }
            return "";
        }));
    }


    protected final Set<String> staticImports = new TreeSet<>();

    {
        chunks.add(new DynamicCharSequence(() -> {
            val content = new StringBuilder();
            if (isNotEmpty(staticImports)) {
                for (val importName : staticImports) {
                    content
                        .append(getStaticImportKeyword())
                        .append(' ')
                        .append(importName)
                        .append(getStatementDelimiter())
                        .append(lineSeparator);
                }
                content.append(lineSeparator);
            }
            return content;
        }));
    }

    @Override
    public void addStaticImport(String className, String memberName) {
        staticImports.add(className + '.' + memberName);
    }


    protected final Set<String> imports = new TreeSet<>();

    {
        chunks.add(new DynamicCharSequence(() -> {
            val content = new StringBuilder();
            if (isNotEmpty(imports)) {
                for (val importName : imports) {
                    content
                        .append(getImportKeyword())
                        .append(' ')
                        .append(importName)
                        .append(getStatementDelimiter())
                        .append(lineSeparator);
                }
                content.append(lineSeparator);
            }
            return content;
        }));
    }

    @Override
    public void addImport(String importName) {
        imports.add(importName);
    }

}
