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

import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Created by joaomartins on 12/14/16.
 */
public class NewFlexibleProjectPanel {

  public static final String AUTOMATICALLY_GENERATED = "Automatically generated";
  public static final String CUSTOM = "Custom";

  private JComboBox configurationMode;
  private JPanel mainPanel;
  private TextFieldWithBrowseButton appYaml;
  private TextFieldWithBrowseButton dockerfile;

  public NewFlexibleProjectPanel() {
    configurationMode.getModel().setSelectedItem(AUTOMATICALLY_GENERATED);
    appYaml.setEnabled(false);
    dockerfile.setEnabled(false);

    configurationMode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (configurationMode.getModel().getSelectedItem().equals(AUTOMATICALLY_GENERATED)) {
          appYaml.setEnabled(false);
          dockerfile.setEnabled(false);
        } else if (configurationMode.getModel().getSelectedItem().equals(CUSTOM)) {
          appYaml.setEnabled(true);
          dockerfile.setEnabled(true);
        }
      }
    });
  }

  public JComponent getComponent() {
    return mainPanel;
  }

  public String getConfigurationMode() {
    return (String) configurationMode.getModel().getSelectedItem();
  }

  public String getAppYaml() {
    return appYaml.getText();
  }

  public String getDockerfile() {
    return dockerfile.getText();
  }

  public void setConfigurationMode(String configurationMode) {
    this.configurationMode.getModel().setSelectedItem(configurationMode);
  }

  public void setAppYaml(String appYaml) {
    this.appYaml.setText(appYaml);
  }

  public void setDockerfile(String dockerfile) {
    this.dockerfile.setText(dockerfile);
  }

  public JComponent getAppYamlComponent() {
    return appYaml;
  }

  public JComponent getDockerfileComponent() {
    return dockerfile;
  }
}
