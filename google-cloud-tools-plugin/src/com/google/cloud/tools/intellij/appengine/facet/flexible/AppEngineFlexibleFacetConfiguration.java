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

import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfiguration.ConfigType;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

/**
 * Created by joaomartins on 12/13/16.
 */
public class AppEngineFlexibleFacetConfiguration implements FacetConfiguration,
    PersistentStateComponent<AppEngineFlexibleFacetConfiguration> {

  private ConfigType configurationType = ConfigType.CUSTOM;
  private String appYamlPath = "";
  private String dockerfilePath = "";

  @Override
  public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext,
      FacetValidatorsManager validatorsManager) {
    return new FacetEditorTab[]{new FlexibleFacetEditor(editorContext.getProject())};
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    // Deprecated, do nothing.
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    // Deprecated, do nothing.
  }

  @Nullable
  @Override
  public AppEngineFlexibleFacetConfiguration getState() {
    return this;
  }

  @Override
  public void loadState(AppEngineFlexibleFacetConfiguration state) {
    configurationType = state.getConfigurationType();
    appYamlPath = state.getAppYamlPath();
    dockerfilePath = state.getDockerfilePath();
  }

  public ConfigType getConfigurationType() {
    return configurationType;
  }

  public String getAppYamlPath() {
    return appYamlPath;
  }

  public String getDockerfilePath() {
    return dockerfilePath;
  }

  public void setConfigurationType(ConfigType configurationType) {
    this.configurationType = configurationType;
  }

  public void setAppYamlPath(String appYamlPath) {
    this.appYamlPath = appYamlPath;
  }

  public void setDockerfilePath(String dockerfilePath) {
    this.dockerfilePath = dockerfilePath;
  }
}
