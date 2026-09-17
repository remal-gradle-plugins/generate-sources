package name.remal.gradle_plugins.generate_sources.task;

import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault(because = "Not worth caching in most situations")
public abstract class GenerateResources
    extends AbstractGenerate {
}
