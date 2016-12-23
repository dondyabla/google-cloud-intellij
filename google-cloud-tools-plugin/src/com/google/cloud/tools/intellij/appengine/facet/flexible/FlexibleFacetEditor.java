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
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfiguration.ConfigType;
import com.google.cloud.tools.intellij.appengine.sdk.CloudSdkPanel;
import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Nullable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Created by joaomartins on 12/14/16.
 */
public class FlexibleFacetEditor extends FacetEditorTab {

  private JComboBox configurationTypeComboBox;
  private JPanel mainPanel;
  private TextFieldWithBrowseButton appYaml;
  private TextFieldWithBrowseButton dockerfile;
  private CloudSdkPanel cloudSdkPanel;
  private JPanel facetConfigurationPanel;
  private AppEngineDeploymentConfiguration deploymentConfiguration;

  public FlexibleFacetEditor() {
    this(null /* project */);
  }

  public FlexibleFacetEditor(Project project) {
    this(project, null /* deploymentConfiguration */);
  }


  public FlexibleFacetEditor(@Nullable Project project,
      @Nullable AppEngineDeploymentConfiguration deploymentConfiguration) {
    configurationTypeComboBox.setModel(new DefaultComboBoxModel(ConfigType.values()));
    configurationTypeComboBox.setSelectedItem(ConfigType.CUSTOM);

    configurationTypeComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        // Freshest information can be found in event rather than configurationTypeComboBox, apparently.
        JComboBox source = (JComboBox) event.getSource();
        if (source.getSelectedItem().equals(ConfigType.AUTO)) {
          appYaml.setEnabled(false);
          dockerfile.setEnabled(false);
        } else if (source.getSelectedItem().equals(ConfigType.CUSTOM)) {
          appYaml.setEnabled(true);
          dockerfile.setEnabled(true);
        }
      }
    });

    appYaml.addBrowseFolderListener(
        GctBundle.message("appengine.flex.config.browse.app.yaml"),
        null /* description */,
        project,
        FileChooserDescriptorFactory.createSingleFileDescriptor().withFileFilter(
            new Condition<VirtualFile>() {
              @Override
              public boolean value(VirtualFile virtualFile) {
                return Comparing.equal(virtualFile.getExtension(), "yaml")
                    || Comparing.equal(virtualFile.getExtension(), "yml");
              }
            }
        )
    );

    dockerfile.addBrowseFolderListener(
        GctBundle.message("appengine.flex.config.browse.dockerfile"),
        null /* description */,
        project,
        FileChooserDescriptorFactory.createSingleFileDescriptor().withFileFilter(
            new Condition<VirtualFile>() {
              @Override
              public boolean value(VirtualFile virtualFile) {
                return Comparing.equal(virtualFile.getExtension(), "");
              }
            }
        )
    );

    this.deploymentConfiguration = deploymentConfiguration;
  }

  @NotNull
  @Override
  public JComponent createComponent() {
    return mainPanel;
  }

  @Override
  public boolean isModified() {
    return !configurationTypeComboBox.getSelectedItem().equals(deploymentConfiguration.getConfigType())
        || !appYaml.getText().equals(deploymentConfiguration.getAppYamlPath())
        || !dockerfile.getText().equals(deploymentConfiguration.getDockerFilePath());
  }

  @Override
  public void reset() {
    configurationTypeComboBox.setSelectedItem(deploymentConfiguration.getConfigType());
    appYaml.setText(deploymentConfiguration.getAppYamlPath());
    dockerfile.setText(deploymentConfiguration.getDockerFilePath());
  }

  @Override
  public void apply() throws ConfigurationException {
    deploymentConfiguration.setConfigType((ConfigType) configurationTypeComboBox.getSelectedItem());
    deploymentConfiguration.setAppYamlPath(appYaml.getText());
    deploymentConfiguration.setDockerFilePath(dockerfile.getText());
  }

  @Override
  public void disposeUIResources() {
    // Do nothing.
  }

  public ConfigType getConfigurationTypeComboBox() {
    return (ConfigType) configurationTypeComboBox.getSelectedItem();
  }

  public String getAppYaml() {
    return appYaml.getText();
  }

  public String getDockerfile() {
    return dockerfile.getText();
  }

  public void setConfigurationTypeComboBox(ConfigType configurationType) {
    configurationTypeComboBox.getModel().setSelectedItem(configurationType);
  }

  public void setAppYaml(String appYaml) {
    this.appYaml.setText(appYaml);
  }

  public void setDockerfile(String dockerfile) {
    this.dockerfile.setText(dockerfile);
  }

  public TextFieldWithBrowseButton getAppYamlComponent() {
    return appYaml;
  }

  public TextFieldWithBrowseButton getDockerfileComponent() {
    return dockerfile;
  }

  public void hideFacetConfiguration() {
    facetConfigurationPanel.setVisible(false);
  }

  @Nls
  @Override
  public String getDisplayName() {
    return GctBundle.getString("appengine.flexible.facet.name");
  }
}
