/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.intellij.appengine.cloud.flexible;

import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfiguration;
import com.google.cloud.tools.intellij.appengine.util.AppEngineUtil;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.RemoteServer;
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.util.CloudDeploymentNameConfiguration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaomartins on 12/20/16.
 */
public class AppEngineFlexibleDeploymentConfiguration
    extends CloudDeploymentNameConfiguration<AppEngineFlexibleDeploymentConfiguration> {

  private String version;
  private boolean promoteVersion;
  private boolean stopPreviousVersion;
  private String appYamlPath;
  private String dockerfilePath;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public boolean isPromoteVersion() {
    return promoteVersion;
  }

  public void setPromoteVersion(boolean promoteVersion) {
    this.promoteVersion = promoteVersion;
  }

  public boolean isStopPreviousVersion() {
    return stopPreviousVersion;
  }

  public void setStopPreviousVersion(boolean stopPreviousVersion) {
    this.stopPreviousVersion = stopPreviousVersion;
  }

  public String getAppYamlPath() {
    return appYamlPath;
  }

  public void setAppYamlPath(String appYamlPath) {
    this.appYamlPath = appYamlPath;
  }

  public String getDockerfilePath() {
    return dockerfilePath;
  }

  public void setDockerfilePath(String dockerfilePath) {
    this.dockerfilePath = dockerfilePath;
  }

  public static class AppEngineFlexibleDeploymentConfigurator
      extends DeploymentConfigurator<AppEngineDeploymentConfiguration,
      AppEngineFlexibleServerConfiguration> {
    private Project project;

    public AppEngineFlexibleDeploymentConfigurator(Project project) {
      this.project = project;
    }

    @NotNull
    @Override
    public List<DeploymentSource> getAvailableDeploymentSources() {
      List<DeploymentSource> deploymentSources = new ArrayList<>();

      deploymentSources.addAll(AppEngineUtil.createArtifactDeploymentSources(project));
      deploymentSources.addAll(AppEngineUtil.createModuleDeploymentSources(project));

      return deploymentSources;
    }

    @NotNull
    @Override
    public AppEngineDeploymentConfiguration createDefaultConfiguration(
        @NotNull DeploymentSource source) {
      return new AppEngineDeploymentConfiguration();
    }

    @Nullable
    @Override
    public SettingsEditor<AppEngineDeploymentConfiguration> createEditor(
        @NotNull DeploymentSource source,
        @NotNull RemoteServer<AppEngineFlexibleServerConfiguration> server) {
      return new AppEngineFlexibleDeploymentEditor(project);
    }
  }
}
