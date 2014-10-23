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


import java.io.File;
import java.net.URL;

/**
 * Default implementation for {@link com.ras.updater.IFileProvider} that takes all values it needs provides in the constructor.
 */
public class DefaultFileProvider implements IFileProvider {
    private String m_fileName;
    private String m_fileExt;
    private URL m_fileUrl;

    public DefaultFileProvider(String fileName, String fileExt, URL fileUrl) {
        if (fileName == null || fileExt == null || fileUrl == null)
            throw new IllegalArgumentException("Parameters cannot contain a null.");
        m_fileName = fileName;
        m_fileExt = fileExt;
        m_fileUrl = fileUrl;
    }

    @Override
    public String getFileName() {
        return m_fileName;
    }

    @Override
    public String getFileExt() {
        return m_fileExt;
    }

    @Override
    public URL getFileUrl() {
        return m_fileUrl;
    }

    @Override
    public File getFileLocation(File parent) {
        return new File(parent, getFileNameExt());
    }
    
    @Override
    public String getFileNameExt() {
        return combineFileNameExt(this);
    }
    
     /**
     * @param fileProvider The provider of information
     * @return {@link #getFileName()} and {@link #getFileExt()} with a period between them
     */
    public static String combineFileNameExt(IFileProvider fileProvider) {
        return fileProvider.getFileName() + "." + fileProvider.getFileExt();
    }
}
