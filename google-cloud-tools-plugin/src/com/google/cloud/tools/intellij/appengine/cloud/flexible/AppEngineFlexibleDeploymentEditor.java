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
import com.google.cloud.tools.intellij.appengine.facet.flexible.NewFlexibleProjectPanel;
import com.google.cloud.tools.intellij.login.CredentialedUser;
import com.google.cloud.tools.intellij.resources.ProjectSelector;
import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextField;

import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Created by joaomartins on 12/20/16.
 */
public class AppEngineFlexibleDeploymentEditor extends
    SettingsEditor<AppEngineDeploymentConfiguration> {

  private JPanel mainPanel;
  private JBTextField version;
  private boolean isVersionSet;
  private JRadioButton promoteVersionRadioButton;
  private JRadioButton stopPreviousVersionRadioButton;
  private NewFlexibleProjectPanel facetPanel;
  private ProjectSelector gcpProjectSelector;
  private JLabel serviceLabel;

  public AppEngineFlexibleDeploymentEditor(Project project) {
    version.getEmptyText().setText(GctBundle.getString("appengine.flex.version.placeholder.text"));

    facetPanel = new NewFlexibleProjectPanel(project);
  }

  @Override
  protected void resetEditorFrom(@NotNull AppEngineDeploymentConfiguration configuration) {
    version.setText(configuration.getVersion());
    promoteVersionRadioButton.setSelected(configuration.isPromote());
    stopPreviousVersionRadioButton.setSelected(configuration.isStopPreviousVersion());
    facetPanel.getAppYamlComponent().setText(configuration.getAppYamlPath());
    facetPanel.getDockerfileComponent().setText(configuration.getDockerFilePath());
    serviceLabel.setText(readServiceNameFromAppYaml(path));
  }

  @Override
  protected void applyEditorTo(@NotNull AppEngineDeploymentConfiguration configuration)
      throws ConfigurationException {
    configuration.setVersion(version.getText());
    configuration.setPromote(promoteVersionRadioButton.isSelected());
    configuration.setStopPreviousVersion(stopPreviousVersionRadioButton.isSelected());
    configuration.setAppYamlPath(facetPanel.getAppYaml());
    configuration.setDockerFilePath(facetPanel.getDockerfile());
    CredentialedUser user = gcpProjectSelector.getSelectedUser();
    if (user != null) {
      configuration.setGoogleUsername(user.getEmail());
    }
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return mainPanel;
  }


}
