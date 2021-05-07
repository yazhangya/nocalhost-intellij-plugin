package dev.nocalhost.plugin.intellij.commands.data;

import java.nio.file.Path;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhctlDevEndOptions extends NhctlGlobalOptions {
    private String deployment;

    public NhctlDevEndOptions(Path kubeConfigPath, String namespace) {
        super(kubeConfigPath, namespace);
    }
}
