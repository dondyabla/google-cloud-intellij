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
import com.google.cloud.tools.intellij.appengine.cloud.CloudSdkAppEngineHelper;

import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager;
import com.intellij.remoteServer.runtime.deployment.DeploymentTask;
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance;

import org.jetbrains.annotations.NotNull;

/**
 * Created by joaomartins on 12/20/16.
 */
public class AppEngineFlexibleServerInstance extends ServerRuntimeInstance {

  @Override
  public void deploy(@NotNull DeploymentTask task, @NotNull DeploymentLogManager logManager,
      @NotNull DeploymentOperationCallback callback) {
    CloudSdkAppEngineHelper helper = new CloudSdkAppEngineHelper(task.getProject());
    helper.createDeployRunner(
        logManager.getMainLoggingHandler(),
        task.getSource(),
        (AppEngineDeploymentConfiguration) task.getConfiguration(),
        callback
    );
  }

  @Override
  public void computeDeployments(@NotNull ComputeDeploymentsCallback callback) {

  }

  @Override
  public void disconnect() {

  }
}
