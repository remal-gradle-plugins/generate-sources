package name.remal.gradle_plugins.generate_sources.task;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.GeneratingOutputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

@RequiredArgsConstructor
class GenerateBinaryFileTaskAction implements Action<Task> {

    private final Provider<? extends CharSequence> relativePathProvider;
    private final Action<? super GeneratingOutputStream> action;

    @Override
    public final void execute(Task task) {
        executeImpl((AbstractGenerate) task);
    }

    @SneakyThrows
    private void executeImpl(AbstractGenerate task) {
        val relativePath = relativePathProvider.get().toString();
        val outputFilePath = task.getOutputDirectory().get().getAsFile().toPath().resolve(relativePath);
        try (val out = new GeneratingOutputStream(outputFilePath)) {
            action.execute(out);
        }
    }

}
