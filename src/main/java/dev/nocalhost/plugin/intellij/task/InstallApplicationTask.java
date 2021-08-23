package dev.nocalhost.plugin.intellij.task;

import com.google.common.collect.Lists;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import dev.nocalhost.plugin.intellij.api.data.Application;
import dev.nocalhost.plugin.intellij.commands.OutputCapturedNhctlCommand;
import dev.nocalhost.plugin.intellij.commands.data.NhctlInstallOptions;
import dev.nocalhost.plugin.intellij.exception.NocalhostNotifier;
import dev.nocalhost.plugin.intellij.topic.NocalhostTreeUpdateNotifier;
import dev.nocalhost.plugin.intellij.utils.ErrorUtil;
import lombok.SneakyThrows;

public class InstallApplicationTask extends BaseBackgroundTask {
    private static final List<String> BOOKINFO_URLS = Lists.newArrayList(
            "https://github.com/nocalhost/bookinfo.git",
            "git@github.com:nocalhost/bookinfo.git",
            "https://e.coding.net/codingcorp/nocalhost/bookinfo.git",
            "git@e.coding.net:codingcorp/nocalhost/bookinfo.git"
    );

    private static final List<String> BOOKINFO_APP_NAME = Lists.newArrayList(
            "bookinfo"
    );

    private final Project project;
    private final Application application;
    private final NhctlInstallOptions opts;

    private final OutputCapturedNhctlCommand outputCapturedNhctlCommand;

    private String productPagePort;

    public InstallApplicationTask(@Nullable Project project, Application application, NhctlInstallOptions opts) {
        super(project, "Installing application: " + application.getContext().getApplicationName(), true);
        this.project = project;
        this.application = application;
        this.opts = opts;

        outputCapturedNhctlCommand = project.getService(OutputCapturedNhctlCommand.class);
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        bookinfo();
        ApplicationManager.getApplication().getMessageBus().syncPublisher(
                NocalhostTreeUpdateNotifier.NOCALHOST_TREE_UPDATE_NOTIFIER_TOPIC).action();

        NocalhostNotifier.getInstance(project).notifySuccess(
                "Application " + application.getContext().getApplicationName() + " installed",
                "");
    }

    private void bookinfo() {
        if (BOOKINFO_APP_NAME.contains(application.getContext().getApplicationName())
                && BOOKINFO_URLS.contains(application.getContext().getApplicationUrl())
                && StringUtils.isNotBlank(productPagePort)) {
            BrowserUtil.browse("http://127.0.0.1:" + productPagePort + "/productpage");
        }
    }

    @Override
    public void onThrowable(@NotNull Throwable e) {
        ErrorUtil.dealWith(this.getProject(), "Nocalhost install devSpace error",
                "Error occurred while installing application", e);
    }

    @SneakyThrows
    @Override
    public void runTask(@NotNull ProgressIndicator indicator) {
        opts.setTask(this);
        outputCapturedNhctlCommand.install(application.getContext().getApplicationName(), opts);
    }
}
