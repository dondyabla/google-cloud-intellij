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
import com.google.cloud.tools.intellij.appengine.cloud.AppEngineDeploymentConfiguration.ConfigType;
import com.google.cloud.tools.intellij.appengine.project.AppEngineProjectService;
import com.google.cloud.tools.intellij.login.CredentialedUser;
import com.google.cloud.tools.intellij.resources.ProjectSelector;
import com.google.cloud.tools.intellij.util.GctBundle;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBTextField;

import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by joaomartins on 12/20/16.
 */
public class AppEngineFlexibleDeploymentEditor extends
    SettingsEditor<AppEngineDeploymentConfiguration> {
  private static String DEFAULT_SERVICE = "default";

  private JPanel mainPanel;
  private JBTextField version;
  private JRadioButton promoteVersionRadioButton;
  private JRadioButton stopPreviousVersionRadioButton;
  private ProjectSelector gcpProjectSelector;
  private JLabel serviceLabel;
  private TextFieldWithBrowseButton appYamlTextField;
  private TextFieldWithBrowseButton dockerfileTextField;

  public AppEngineFlexibleDeploymentEditor(Project project) {
    version.getEmptyText().setText(GctBundle.getString("appengine.flex.version.placeholder.text"));
    appYamlTextField.addBrowseFolderListener(
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

    dockerfileTextField.addBrowseFolderListener(
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

    appYamlTextField.getTextField().getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updateServiceName();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateServiceName();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateServiceName();
      }
    });
  }

  @Override
  protected void resetEditorFrom(@NotNull AppEngineDeploymentConfiguration configuration) {
    version.setText(configuration.getVersion());
    promoteVersionRadioButton.setSelected(configuration.isPromote());
    stopPreviousVersionRadioButton.setSelected(configuration.isStopPreviousVersion());
    appYamlTextField.setText(configuration.getAppYamlPath());
    dockerfileTextField.setText(configuration.getDockerFilePath());
    gcpProjectSelector.setText(configuration.getCloudProjectName());
    updateServiceName();
  }

  @Override
  protected void applyEditorTo(@NotNull AppEngineDeploymentConfiguration configuration)
      throws ConfigurationException {
    configuration.setVersion(version.getText());
    configuration.setPromote(promoteVersionRadioButton.isSelected());
    configuration.setStopPreviousVersion(stopPreviousVersionRadioButton.isSelected());
    configuration.setAppYamlPath(appYamlTextField.getText());
    configuration.setDockerFilePath(dockerfileTextField.getText());
    configuration.setCloudProjectName(gcpProjectSelector.getText());
    CredentialedUser user = gcpProjectSelector.getSelectedUser();
    if (user != null) {
      configuration.setGoogleUsername(user.getEmail());
    }
    configuration.setConfigType(ConfigType.CUSTOM);
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return mainPanel;
  }

  private void updateServiceName() {
    String service = AppEngineProjectService.getInstance().getServiceNameFromAppYaml(
        appYamlTextField.getText());
    serviceLabel.setText(service != null ? service : DEFAULT_SERVICE);
  }
}
