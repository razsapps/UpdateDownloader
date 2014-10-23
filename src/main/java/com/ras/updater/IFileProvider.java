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
 * Provides information about a file that can be updated
 */
public interface IFileProvider {
    /**
     * @return Returns the file name, but not the extension of the file
     */
    public String getFileName();

    /**
     * @return the file extension of the file
     */
    public String getFileExt();

    /**
     * @return The file url where this file can be downloaded from
     */
    public URL getFileUrl();

    /**
     * @param parent The parent directory of the file download destination
     * @return The file location where to put the successfully downloaded file
     */
    public File getFileLocation(File parent);

    /**
     * @return A combination of {@link #getFileName()} and {@link #getFileExt()} 
     * representing the full filename
     */
    public String getFileNameExt();
}
