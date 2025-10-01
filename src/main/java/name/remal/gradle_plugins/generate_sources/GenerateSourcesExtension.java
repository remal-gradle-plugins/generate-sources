package name.remal.gradle_plugins.generate_sources;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PUBLIC;
import static name.remal.gradle_plugins.toolkit.ActionUtils.doNothingAction;
import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

@Getter
@RequiredArgsConstructor(access = PUBLIC, onConstructor_ = {@Inject})
public abstract class GenerateSourcesExtension {

    @Getter(NONE)
    private final Map<String, GenerateSourcesSourceSet> sourceSetTargets = new LinkedHashMap<>();

    public GenerateSourcesSourceSet forSourceSet(
        NamedDomainObjectProvider<? extends SourceSet> sourceSetProvider,
        Action<? super GenerateSourcesSourceSet> action
    ) {
        var target = sourceSetTargets.computeIfAbsent(sourceSetProvider.getName(), __ ->
            getObjects().newInstance(
                GenerateSourcesSourceSet.class,
                sourceSetProvider
            )
        );
        action.execute(target);
        return target;
    }

    public GenerateSourcesSourceSet forSourceSet(
        NamedDomainObjectProvider<? extends SourceSet> sourceSetProvider
    ) {
        return forSourceSet(sourceSetProvider, doNothingAction());
    }

    public GenerateSourcesSourceSet forSourceSet(
        String sourceSetName,
        Action<? super GenerateSourcesSourceSet> action
    ) {
        if (!getProject().getPluginManager().hasPlugin("java")) {
            throw new IllegalStateException("`java` plugin is not applied");
        }

        var sourceSets = getProject().getExtensions().getByType(SourceSetContainer.class);
        var sourceSetProvider = sourceSets.named(sourceSetName);
        return forSourceSet(sourceSetProvider, action);
    }

    public GenerateSourcesSourceSet forSourceSet(
        String sourceSetName
    ) {
        return forSourceSet(sourceSetName, doNothingAction());
    }

    public GenerateSourcesSourceSet forSourceSet(
        SourceSet sourceSet,
        Action<? super GenerateSourcesSourceSet> action
    ) {
        return forSourceSet(sourceSet.getName(), action);
    }

    public GenerateSourcesSourceSet forSourceSet(
        SourceSet sourceSet
    ) {
        return forSourceSet(sourceSet, doNothingAction());
    }


    public GenerateSourcesSourceSet forMainSourceSet(Action<? super GenerateSourcesSourceSet> action) {
        return forSourceSet(MAIN_SOURCE_SET_NAME, action);
    }

    public GenerateSourcesSourceSet forMainSourceSet() {
        return forMainSourceSet(doNothingAction());
    }

    public GenerateSourcesSourceSet getForMainSourceSet() {
        return forMainSourceSet();
    }


    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract Project getProject();

}
