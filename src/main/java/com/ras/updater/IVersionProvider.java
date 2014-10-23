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

/**
 * This interface will provide version information for files.
 *
 * Note: Version information are Strings due to allow the ability to use checksums as the version if desired
 */
public interface IVersionProvider {
    /**
     * @param fileNameExt The file we are interested in
     * @return The current version information of the file on the user's system. null if the file does not currently exist.
     */
    public String getCurrentVersion(String fileNameExt);

    /**
     * @param fileNameExt The file we are interested in
     * @return The newest version of the file available for download/update. null if the file should no longer exist.
     */
    public String getNewestVersion(String fileNameExt);
}
