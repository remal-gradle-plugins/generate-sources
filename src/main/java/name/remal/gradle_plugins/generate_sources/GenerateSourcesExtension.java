package name.remal.gradle_plugins.generate_sources;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PUBLIC;

import java.util.IdentityHashMap;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSet;

@Getter
@RequiredArgsConstructor(access = PUBLIC, onConstructor_ = {@Inject})
public abstract class GenerateSourcesExtension {

    @Getter(NONE)
    private final IdentityHashMap<SourceSet, GenerateSourcesSourceSet> sourceSetTargets = new IdentityHashMap<>();

    public GenerateSourcesSourceSet forSourceSet(SourceSet sourceSet) {
        return sourceSetTargets.computeIfAbsent(sourceSet, __ ->
            getObjects().newInstance(GenerateSourcesSourceSet.class, sourceSet)
        );
    }

    public GenerateSourcesSourceSet forSourceSet(SourceSet sourceSet, Action<GenerateSourcesSourceSet> action) {
        val target = forSourceSet(sourceSet);
        action.execute(target);
        return target;
    }


    @Inject
    protected abstract ObjectFactory getObjects();

}
