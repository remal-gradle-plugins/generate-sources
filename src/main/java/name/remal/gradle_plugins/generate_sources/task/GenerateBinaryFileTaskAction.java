package name.remal.gradle_plugins.generate_sources.task;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Describable;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

@RequiredArgsConstructor
class GenerateBinaryFileTaskAction implements Action<Task>, Describable {

    private final Provider<String> relativePathProvider;
    private final Action<? super GeneratingOutputStream> action;

    @Override
    public final void execute(Task task) {
        executeImpl((AbstractGenerate) task);
    }

    @SneakyThrows
    private void executeImpl(AbstractGenerate task) {
        val relativePath = relativePathProvider.get();
        val outputFilePath = task.getOutputDirectory().get().getAsFile().toPath().resolve(relativePath);
        try (val out = new GeneratingOutputStream(outputFilePath)) {
            action.execute(out);
        }
    }

    @Override
    public String getDisplayName() {
        return "Generate " + relativePathProvider.get();
    }

}
