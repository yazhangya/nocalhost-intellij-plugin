package dev.nocalhost.plugin.intellij.commands.data;

import java.util.Objects;

import dev.nocalhost.plugin.intellij.api.data.DevSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AliveDeployment {
    private DevSpace devSpace;
    private String deployment;
    private String projectPath;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AliveDeployment that = (AliveDeployment) o;
        if (this.devSpace == null) return false;
        if (that.devSpace == null) return false;
        return Objects.equals(devSpace.getContext().getApplicationName(), that.devSpace.getContext().getApplicationName()) && Objects.equals(deployment, that.deployment) && Objects.equals(projectPath, that.projectPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devSpace.getContext().getApplicationName(), deployment, projectPath);
    }
}
