package name.remal.gradle_plugins.generate_sources.task;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import name.remal.gradle_plugins.generate_sources.generators.GeneratingOutputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;

@RequiredArgsConstructor
class GenerateBinaryFileTaskAction implements Action<Task> {

    private final String relativePath;
    private final Action<GeneratingOutputStream> action;

    @Override
    public final void execute(Task task) {
        executeImpl((AbstractGenerate<?>) task);
    }

    @SneakyThrows
    private void executeImpl(AbstractGenerate<?> task) {
        val outputFilePath = task.getOutputDirectory().get().getAsFile().toPath().resolve(relativePath);
        try (val out = new GeneratingOutputStream(outputFilePath)) {
            action.execute(out);
        }
    }

}
