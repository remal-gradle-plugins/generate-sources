package name.remal.gradle_plugins.generate_sources.task;

import static java.nio.charset.StandardCharsets.UTF_8;

import name.remal.gradle_plugins.generate_sources.generators.GeneratingWriter;
import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;

public abstract class AbstractGenerateText
    extends AbstractGenerate {

    @Input
    public abstract Property<String> getEncoding();

    {
        getEncoding().convention(UTF_8.name());
    }

    @Override
    public void textFile(String relativePath, Action<GeneratingWriter> action) {
        super.textFile(relativePath, getEncoding().get(), action);
    }

}
