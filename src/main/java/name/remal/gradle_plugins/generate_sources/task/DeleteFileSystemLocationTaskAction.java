package name.remal.gradle_plugins.generate_sources.task;

import static name.remal.gradle_plugins.toolkit.PathUtils.deleteRecursively;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Action;
import org.gradle.api.Task;

@RequiredArgsConstructor
class DeleteFileSystemLocationTaskAction implements Action<Task> {

    private final File location;

    @Override
    public void execute(Task task) {
        deleteRecursively(location.toPath());
    }

}
