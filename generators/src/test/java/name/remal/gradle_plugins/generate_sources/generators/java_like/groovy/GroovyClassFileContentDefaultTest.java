package name.remal.gradle_plugins.generate_sources.generators.java_like.groovy;

import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class GroovyClassFileContentDefaultTest {

    @Test
    void simple() {
        var content = new GroovyClassFileContentDefault("pkg", "Logic", null, null);
        content.addImport(List.class);
        content.addStaticImport(Arrays.class, "asList");
        content.block("class " + content.getSimpleName(), clazz -> {
            clazz.line();
            clazz.suppressWarningsLine("unchecked", "rawtypes");
            clazz.block("static List<Integer> execute()", method -> {
                method.line("return asList(");
                method.indent(ret -> {
                    ret.line("1,");
                    ret.line("2,");
                    ret.line("3,");
                });
                method.line(")");
            });
            clazz.line();
        });

        var contentString = content.toString();

        assertThat(contentString).isEqualTo(join("\n", new String[]{
            "package pkg",
            "",
            "import static java.util.Arrays.asList",
            "",
            "import java.util.List",
            "",
            "class Logic {",
            "",
            "    @SuppressWarnings([\"unchecked\", \"rawtypes\"])",
            "    static List<Integer> execute() {",
            "        return asList(",
            "            1,",
            "            2,",
            "            3,",
            "        )",
            "    }",
            "",
            "}",
            "",
        }));
    }

}
