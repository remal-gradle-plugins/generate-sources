package name.remal.gradle_plugins.generate_sources.generators.java_like.kotlin;

import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class KotlinClassFileContentDefaultTest {

    @Test
    void simple() {
        val content = new KotlinClassFileContentDefault("pkg", "Logic", null, null);
        content.addImport(List.class);
        content.addStaticImport(Arrays.class, "asList");
        content.block("class " + content.getSimpleName(), clazz -> {
            clazz.line();
            clazz.suppressWarningsLine("unchecked", "rawtypes");
            clazz.block("fun execute(): List<Int>", method -> {
                method.line("return listOf(");
                method.indent(ret -> {
                    ret.line("1,");
                    ret.line("2,");
                    ret.line("3");
                });
                method.line(")");
            });
            clazz.line();
        });

        val contentString = content.toString();

        assertThat(contentString).isEqualTo(join("\n", new String[]{
            "package pkg",
            "",
            "import java.util.Arrays.asList",
            "import java.util.List",
            "",
            "class Logic {",
            "",
            "    @Suppress(\"unchecked\", \"rawtypes\")",
            "    fun execute(): List<Int> {",
            "        return listOf(",
            "            1,",
            "            2,",
            "            3",
            "        )",
            "    }",
            "",
            "}",
            "",
        }));
    }

}
