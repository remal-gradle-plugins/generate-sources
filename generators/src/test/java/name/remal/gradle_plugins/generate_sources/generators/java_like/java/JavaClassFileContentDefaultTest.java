package name.remal.gradle_plugins.generate_sources.generators.java_like.java;

import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class JavaClassFileContentDefaultTest {

    @Test
    void simple() {
        val content = new JavaClassFileContentDefault("pkg", "Logic", null, null);
        content.addImport(List.class);
        content.addStaticImport(Arrays.class, "asList");
        content.block("public class " + content.getSimpleName(), clazz -> {
            clazz.line();
            clazz.block("public static List<Integer> execute()", method -> {
                method.line("return asList(");
                method.indent(ret -> {
                    ret.line("1,");
                    ret.line("2,");
                    ret.line("3");
                });
                method.line(");");
            });
            clazz.line();
        });

        val contentString = content.toString();

        assertThat(contentString).isEqualTo(join("\n", new String[]{
            "package pkg;",
            "",
            "import static java.util.Arrays.asList;",
            "",
            "import java.util.List;",
            "",
            "public class Logic {",
            "",
            "    public static List<Integer> execute() {",
            "        return asList(",
            "            1,",
            "            2,",
            "            3",
            "        );",
            "    }",
            "",
            "}",
            "",
        }));
    }

}
