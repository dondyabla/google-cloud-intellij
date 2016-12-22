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

package com.google.cloud.tools.intellij.appengine.facet.flexible;

import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfiguration;
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineEnvironment;
import com.google.cloud.tools.intellij.appengine.cloud.flexible.AppEngineFlexibleCloudType;
import com.google.cloud.tools.intellij.appengine.cloud.flexible.AppEngineFlexibleDeploymentConfiguration;
import com.google.cloud.tools.intellij.appengine.cloud.flexible.AppEngineFlexibleServerConfiguration;
import com.google.cloud.tools.intellij.appengine.project.AppEngineProjectService;
import com.google.cloud.tools.intellij.appengine.util.AppEngineUtil;
import com.google.cloud.tools.intellij.login.CredentialedUser;
import com.google.cloud.tools.intellij.login.Services;
import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModelImpl;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.FacetsProvider;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.impl.artifacts.ArtifactUtil;
import com.intellij.remoteServer.ServerType;
import com.intellij.remoteServer.configuration.RemoteServer;
import com.intellij.remoteServer.configuration.RemoteServersManager;
import com.intellij.remoteServer.impl.configuration.deployment.DeployToServerConfigurationType;
import com.intellij.remoteServer.impl.configuration.deployment.DeployToServerConfigurationTypesRegistrar;
import com.intellij.remoteServer.impl.configuration.deployment.DeployToServerRunConfiguration;
import com.intellij.util.containers.ContainerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import javax.swing.JComponent;

/**
 * Created by joaomartins on 12/13/16.
 */
public class AppEngineFlexibleSupportProvider extends FrameworkSupportInModuleProvider {

  @NotNull
  @Override
  public FrameworkTypeEx getFrameworkType() {
    return AppEngineFlexibleFrameworkType.getFrameworkType();
  }

  @NotNull
  @Override
  public FrameworkSupportInModuleConfigurable createConfigurable(
      @NotNull FrameworkSupportModel model) {
    return new AppEngineFlexibleSupportConfigurable(model);
  }

  @Override
  public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
    return moduleType instanceof JavaModuleType;
  }

  @Override
  public boolean isSupportAlreadyAdded(@NotNull Module module,
      @NotNull FacetsProvider facetsProvider) {
    Collection<AppEngineFlexibleFacet> facets =
        facetsProvider.getFacetsByType(module, AppEngineFlexibleFacetType.ID);

    return facets != null && !facets.isEmpty();
  }

  class AppEngineFlexibleSupportConfigurable extends FrameworkSupportInModuleConfigurable {
    private NewFlexibleProjectPanel newProjectPanel;

    public AppEngineFlexibleSupportConfigurable(FrameworkSupportModel model) {
      newProjectPanel = new NewFlexibleProjectPanel(model.getProject());
      String contentRoot = ((FrameworkSupportModelImpl) model).getBaseDirectoryForLibrariesPath();
      // TODO(joaomartins): Attempt to discover actual existing files.
      newProjectPanel.setAppYaml(contentRoot + "/src/main/appengine/app.yaml");
      newProjectPanel.setDockerfile(contentRoot + "/src/main/docker/Dockerfile");
    }

    @Nullable
    @Override
    public JComponent createComponent() {
      return newProjectPanel.getComponent();
    }

    @Override
    public void onFrameworkSelectionChanged(boolean selected) {
      if (selected) {
        newProjectPanel.getAppYamlComponent().setEnabled(
            newProjectPanel.getConfigurationMode().equals(
                AppEngineFlexibleFacetConfiguration.CUSTOM));
        newProjectPanel.getDockerfileComponent().setEnabled(
            newProjectPanel.getConfigurationMode().equals(
                AppEngineFlexibleFacetConfiguration.CUSTOM));
      }
    }

    @Override
    public void addSupport(@NotNull Module module, @NotNull ModifiableRootModel rootModel,
        @NotNull ModifiableModelsProvider modifiableModelsProvider) {
      FacetType<AppEngineFlexibleFacet, AppEngineFlexibleFacetConfiguration> facetType =
          AppEngineFlexibleFacet.getFacetType();
      AppEngineFlexibleFacet facet = FacetManager.getInstance(module).addFacet(
          facetType, facetType.getPresentableName(), null /* underlyingFacet */);

      facet.getConfiguration().setConfigurationMode(newProjectPanel.getConfigurationMode());
      facet.getConfiguration().setAppYamlPath(newProjectPanel.getAppYaml());
      facet.getConfiguration().setDockerfilePath(newProjectPanel.getDockerfile());

      // Only adds deployment run configuration for now. Stackdriver debugger to follow.
      setupDeploymentRunConfiguration(module, facet);
    }

    private void setupDeploymentRunConfiguration(Module module, AppEngineFlexibleFacet facet) {
      RunManager runManager = RunManager.getInstance(module.getProject());
      AppEngineFlexibleCloudType serverType =
          ServerType.EP_NAME.findExtension(AppEngineFlexibleCloudType.class);
      DeployToServerConfigurationType configurationType
          = DeployToServerConfigurationTypesRegistrar.getDeployConfigurationType(serverType);

      RunnerAndConfigurationSettings settings = runManager.createRunConfiguration(
          GctBundle.getString("appengine.flex.deployment.name") + " " + module.getName(),
          configurationType.getFactory());

      // Sets the GAE Flex server, if any exists, in the run config.
      DeployToServerRunConfiguration<?, AppEngineDeploymentConfiguration> runConfiguration =
          (DeployToServerRunConfiguration<?, AppEngineDeploymentConfiguration>)
              settings.getConfiguration();
      RemoteServer<AppEngineFlexibleServerConfiguration> server =
          ContainerUtil.getFirstItem(RemoteServersManager.getInstance().getServers(serverType));
      if (server != null) {
        runConfiguration.setServerName(server.getName());
      }

      // Sets the first found Flex deployable, if any exists, in the run config.
//      AppEngineProjectService projectService = AppEngineProjectService.getInstance();
//      for (Artifact artifact : ArtifactUtil.getArtifactsContainingModuleOutput(module)) {
//        if (projectService.isAppEngineFlexArtifactType(artifact)) {
//          runConfiguration.setDeploymentSource(
//              AppEngineUtil.createArtifactDeploymentSource(module.getProject(), artifact,
//                  AppEngineEnvironment.APP_ENGINE_FLEX));
//          break;
//        }
//      }

      // Copies the specified app.yaml and Dockerfile paths to the deployment run config.
      AppEngineDeploymentConfiguration deployConfiguration =
          new AppEngineDeploymentConfiguration();
      deployConfiguration.setAppYamlPath(facet.getConfiguration().getAppYamlPath());
      deployConfiguration.setDockerFilePath(facet.getConfiguration().getDockerfilePath());

      // Set logged in user.
      CredentialedUser user = Services.getLoginService().getActiveUser();
      if (user != null) {
        deployConfiguration.setGoogleUsername(user.getEmail());
      }

      runConfiguration.setDeploymentConfiguration(deployConfiguration);

      runManager.addConfiguration(settings, false /* shared */);
    }
  }
}
