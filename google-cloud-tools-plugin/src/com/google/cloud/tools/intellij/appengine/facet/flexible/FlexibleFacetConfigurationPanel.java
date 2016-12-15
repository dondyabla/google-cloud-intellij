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

import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.facet.ui.FacetEditorTab;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * This form only contains the configuration files form defined in {@link NewFlexibleProjectPanel}
 * for now, but should be complemented with additional Flexible parameters in the future.
 */
public class FlexibleFacetConfigurationPanel extends FacetEditorTab {

  private AppEngineFlexibleFacetConfiguration configuration;
  private JPanel mainPanel;
  private NewFlexibleProjectPanel configFilesPanel;

  public FlexibleFacetConfigurationPanel(AppEngineFlexibleFacetConfiguration configuration) {
    this.configuration = configuration;
  }

  @NotNull
  @Override
  public JComponent createComponent() {
    return mainPanel;
  }

  @Override
  public boolean isModified() {
    return !configFilesPanel.getConfigurationMode().equals(configuration.getConfigurationMode())
        || !configFilesPanel.getAppYaml().equals(configuration.getAppYamlPath())
        || !configFilesPanel.getDockerfile().equals(configuration.getDockerfilePath());
  }

  @Override
  public void apply() {
    configuration.setConfigurationMode(configFilesPanel.getConfigurationMode());
    configuration.setAppYamlPath(configFilesPanel.getAppYaml());
    configuration.setDockerfilePath(configFilesPanel.getAppYaml());
  }

  @Override
  public void reset() {
    configFilesPanel.setConfigurationMode(configuration.getConfigurationMode());
    configFilesPanel.setAppYaml(configuration.getAppYamlPath().toString());
    configFilesPanel.setDockerfile(configuration.getDockerfilePath().toString());
  }

  @Override
  public void disposeUIResources() {
    // Do nothing.
  }

  @Nls
  @Override
  public String getDisplayName() {
    return GctBundle.getString("appengine.flexible.facet.name");
  }
}
