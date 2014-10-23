/*
 * Copyright 2014 Richard So
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ras.updater;

import java.util.HashMap;

/**
 * Default implementation of {@link com.ras.updater.IVersionProvider} that allows a user to add each file's current or newest version one at at time.
 */
public class DefaultVersionProvider implements IVersionProvider {
    private HashMap<String, String> m_currentVersions = new HashMap<String, String>();
    private HashMap<String, String> m_newestVersions = new HashMap<String, String>();

    public void addCurrentVersion(String fileNameExt, String version) {
        m_currentVersions.put(fileNameExt, version);
    }

    public void addNewestVersion(String fileNameExt, String version) {
        m_newestVersions.put(fileNameExt, version);
    }
    
    @Override
    public String getCurrentVersion(String fileNameExt) {
        return m_currentVersions.get(fileNameExt);
    }

    @Override
    public String getNewestVersion(String fileNameExt) {
        return m_newestVersions.get(fileNameExt);
    }
}
