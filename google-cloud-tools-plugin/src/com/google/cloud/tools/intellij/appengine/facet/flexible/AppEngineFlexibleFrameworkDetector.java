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

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;

import org.jetbrains.annotations.NotNull;

/**
 * Created by joaomartins on 12/14/16.
 */
public class AppEngineFlexibleFrameworkDetector extends
    FacetBasedFrameworkDetector<AppEngineFlexibleFacet, AppEngineFlexibleFacetConfiguration> {
  public static String APP_YAML_FILE_NAME = "app.yaml";
  public static String DOCKERFILE_FILE_NAME = "Dockerfile";

  public AppEngineFlexibleFrameworkDetector() {
    super("appengine-flexible");
  }

  @Override
  public FacetType<AppEngineFlexibleFacet, AppEngineFlexibleFacetConfiguration> getFacetType() {
    return FacetType.findInstance(AppEngineFlexibleFacetType.class);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return FileTypes.PLAIN_TEXT;
  }

  @NotNull
  @Override
  public ElementPattern<FileContent> createSuitableFilePattern() {
    return FileContentPattern.fileContent().withName(APP_YAML_FILE_NAME).andOr(
        FileContentPattern.fileContent().withName(DOCKERFILE_FILE_NAME)
    );
  }
}
