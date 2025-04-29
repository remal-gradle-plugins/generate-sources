package name.remal.gradle_plugins.generate_sources.task;

import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;

@CacheableTask
public abstract class AbstractGenerateText
    extends AbstractGenerate {

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getEncoding();

    @Override
    public void textFile(Provider<String> relativePath, Action<? super GeneratingWriter> action) {
        super.textFile(relativePath, getEncoding(), action);
    }

    @Override
    public void textFile(String relativePath, Action<? super GeneratingWriter> action) {
        textFile(fixedProvider(relativePath), action);
    }

}
