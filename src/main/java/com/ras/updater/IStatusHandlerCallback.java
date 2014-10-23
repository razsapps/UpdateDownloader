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


public interface IStatusHandlerCallback {
    /**
     * Called when a throwable is thrown during execution
     * @param t The throwable that occurred during execution
     */
    public void handleError(Throwable t);

    /**
     * Update has been completed
     * @param updated true if any files have been updated false otherwise
     */
    public void handleComplete(Boolean updated);

    /**
     * Called when a file has been successfully updated
     * @param fileNameExt The file name along with it's extension. See {@link com.ras.updater.IFileProvider#getFileNameExt(IFileProvider)} for more details
     * @param newestVersion The newest version of the file just updated
     */
    public void fileUpdate(String fileNameExt, String newestVersion);

    public static class NullStatusHandlerCallback implements IStatusHandlerCallback {
        @Override
        public void handleError(Throwable t) {
            t.printStackTrace();
        }
        @Override
        public void handleComplete(Boolean updated) {
        }
        @Override
        public void fileUpdate(String fileNameExt, String newestVersion) {
        }
    }
}
