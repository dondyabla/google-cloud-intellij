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

import com.google.cloud.tools.intellij.appengine.cloud.AppEngineCloudConfigurable;
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineCloudType.AppEngineServerConnector;
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfigurator;
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineServerConfiguration;
import com.google.cloud.tools.intellij.ui.GoogleCloudToolsIcons;
import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.RemoteServerConfigurable;
import com.intellij.remoteServer.ServerType;
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator;
import com.intellij.remoteServer.runtime.ServerConnector;
import com.intellij.remoteServer.runtime.ServerTaskExecutor;

import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * Created by joaomartins on 12/20/16.
 */
public class AppEngineFlexibleCloudType extends ServerType<AppEngineServerConfiguration> {

  private static final String ID = "gcp-app-engine-flexible";

  public AppEngineFlexibleCloudType() {
    super(ID);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return GctBundle.getString("appengine.flexible.facet.name");
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return GoogleCloudToolsIcons.APP_ENGINE;
  }

  @NotNull
  @Override
  public AppEngineServerConfiguration createDefaultConfiguration() {
    return new AppEngineServerConfiguration();
  }

  @NotNull
  @Override
  public DeploymentConfigurator<?, AppEngineServerConfiguration>
  createDeploymentConfigurator(Project project) {
    return new AppEngineDeploymentConfigurator(project);
  }

  @NotNull
  @Override
  public RemoteServerConfigurable createServerConfigurable(
      @NotNull AppEngineServerConfiguration configuration) {
    return new AppEngineCloudConfigurable();
  }

  @NotNull
  @Override
  public ServerConnector<?> createConnector(
      @NotNull AppEngineServerConfiguration configuration,
      @NotNull ServerTaskExecutor asyncTasksExecutor) {
    return new AppEngineServerConnector();
  }
}
