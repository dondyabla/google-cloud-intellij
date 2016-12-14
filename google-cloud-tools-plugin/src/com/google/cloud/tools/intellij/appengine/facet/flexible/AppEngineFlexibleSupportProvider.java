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

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    return new AppEngineFlexibleSupportConfigurable();
  }

  @Override
  public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
    return moduleType instanceof JavaModuleType;
  }

  class AppEngineFlexibleSupportConfigurable extends FrameworkSupportInModuleConfigurable {
    private NewFlexibleProjectPanel newProjectPanel = new NewFlexibleProjectPanel();

    @Nullable
    @Override
    public JComponent createComponent() {
      return newProjectPanel.getComponent();
    }

    @Override
    public void addSupport(@NotNull Module module, @NotNull ModifiableRootModel rootModel,
        @NotNull ModifiableModelsProvider modifiableModelsProvider) {

    }
  }
}
