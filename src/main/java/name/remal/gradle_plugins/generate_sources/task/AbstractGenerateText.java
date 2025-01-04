package name.remal.gradle_plugins.generate_sources.task;

import name.remal.gradle_plugins.generate_sources.generators.GeneratingWriter;
import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;

public abstract class AbstractGenerateText
    extends AbstractGenerate {

    @Input
    public abstract Property<String> getEncoding();

    @Override
    public void textFile(Provider<? extends CharSequence> relativePath, Action<? super GeneratingWriter> action) {
        super.textFile(relativePath, getEncoding(), action);
    }

    @Override
    public void textFile(CharSequence relativePath, Action<? super GeneratingWriter> action) {
        textFile(provider(relativePath), action);
    }

}
