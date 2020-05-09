package template.metadata;

import javax.annotation.Nonnull;

public class MVPMetaData {
    @Nonnull public String name;
    @Nonnull public String prefix;
    @Nonnull public String packageName;
    @Nonnull public String path;
    @Nonnull public String rootPackage;
    @Nonnull public String layoutPath;
    public boolean useFragment;

    public MVPMetaData(@Nonnull String name, @Nonnull String prefix, @Nonnull String packageName, @Nonnull String path,
                       @Nonnull String rootPackage, @Nonnull String layoutPath, boolean useFragment) {
        this.name = name;
        this.prefix = prefix;
        this.packageName = packageName;
        this.path = path;
        this.rootPackage = rootPackage;
        this.layoutPath = layoutPath;
        this.useFragment = useFragment;
    }
}
