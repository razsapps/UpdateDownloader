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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * This class will download any file that has been updated and replace it
 */
public class Downloader {
    public static final int FILE_TIMEOUT = 3000;
    private IStatusHandlerCallback m_statusCallback;
    private IFileProvider[] m_fileProviders;
    private IVersionProvider m_versionProvider;
    private int m_connectionTimeout = FILE_TIMEOUT;
    private int m_readTimeout = FILE_TIMEOUT;
    private long m_downloadTimeout = 10;
    private TimeUnit m_downloadTimeUnit = TimeUnit.MINUTES;
    private File m_downloadDestination;

    /**
     * Constructs an instance of Downloader
     * @param downloadDestination The parent directly where all successfully downloaded files should end up
     * @param versionProvider A container to inform if there has been an update or not to a specific version of a file
     * @param fileProviders The list of files that need to be updated
     */
    public Downloader(File downloadDestination, IVersionProvider versionProvider, IFileProvider... fileProviders) {
        this(new IStatusHandlerCallback.NullStatusHandlerCallback(), downloadDestination, versionProvider, fileProviders);
    }

    /**
     * Constructs an instance of Downloader
     * @param statusCallback A callback to be informed of different events during the update process
     * @param downloadDestination The parent directly where all successfully downloaded files should end up
     * @param versionProvider A container to inform if there has been an update or not to a specific version of a file
     * @param fileProviders The list of files that need to be updated
     */
    public Downloader(IStatusHandlerCallback statusCallback, File downloadDestination, IVersionProvider versionProvider, IFileProvider... fileProviders) {
        m_statusCallback = statusCallback;
        m_downloadDestination = downloadDestination;
        m_versionProvider = versionProvider;
        m_fileProviders = fileProviders;

        if (m_fileProviders == null || m_fileProviders.length == 0)
            throw new IllegalArgumentException("You must provide at least one file to update.");
    }

    /**
     * Change the timeouts for downloading a file.  Default is 3 seconds.
     * @param connectionTimeout Timeout for how long to connect to the URL of the file
     * @param readTimeout Timeout for how long to read the file data in after the URL has been connected to
     */
    public void setFileTimeouts(int connectionTimeout, int readTimeout) {
        m_connectionTimeout = connectionTimeout;
        m_readTimeout = readTimeout;
    }

    /**
     * Change the timeout for the {@link #update()}. Default is 10 minutes.
     * @param timeout The quantity of {@link java.util.concurrent.TimeUnit} to wait for
     * @param timeUnit The time unit to use during the timeouts
     */
    public void setDownloadTimeout(long timeout, TimeUnit timeUnit) {
        m_downloadTimeout = timeout;
        m_downloadTimeUnit = timeUnit;
    }

    /**
     * This method will check for updates on all {@link #m_fileProviders} and download anything with an update.
     * @return true if at least one file was updated or false if no files were updated
     */
    public boolean update() {
        ArrayList<Future<Boolean>> results = new ArrayList<Future<Boolean>>();
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (IFileProvider fileProvider : m_fileProviders) {
            FileUpdaterCallable task = new FileUpdaterCallable(fileProvider);
            results.add(es.submit(task));
        }
        es.shutdown();
        try {
            if (!es.awaitTermination(m_downloadTimeout, m_downloadTimeUnit))
                es.shutdownNow();
        } catch (InterruptedException e) {
            m_statusCallback.handleError(e);
            es.shutdownNow();
            Thread.currentThread().interrupt();
        }

        //Loop through the results for update values
        for (Future<Boolean> result: results) {
            try {
                if (result.isDone() && result.get() != null && result.get())
                    return true;
            } catch (InterruptedException e) {
                //This should never happen
                m_statusCallback.handleError(e);
            } catch (ExecutionException e) {
                m_statusCallback.handleError(e);
            }
        }

        return false;
    }

    /**
     * Class to actually check the version of each {@link com.ras.updater.IFileProvider} to see if it has been updated.
     * If there has been an update the file is downloaded to a temp location then moved to its final resting place when
     * finished.  If the {@link com.ras.updater.IVersionProvider#getNewestVersion(String)} is null that means the file
     * should be deleted.
     */
    private class FileUpdaterCallable implements Callable<Boolean> {
        private IFileProvider m_fileProvider;

        public FileUpdaterCallable(IFileProvider fileProvider) {
            m_fileProvider = fileProvider;
        }

        @Override
        public Boolean call() throws Exception {
            String id = m_fileProvider.getFileNameExt();
            String currentVersion = m_versionProvider.getCurrentVersion(id);
            String newestVersion = m_versionProvider.getNewestVersion(id);

            //Exit if there is nothing to update
            if (newestVersion == currentVersion || (currentVersion != null && currentVersion.equals(newestVersion)))
                return false;

            File file = m_fileProvider.getFileLocation(m_downloadDestination);

            //If the newest version calls for a delete then delete the file
            if (newestVersion == null) {
                if (!file.exists() || !file.delete())
                    throw new IOException("Could not delete " + id);
                return true;
            }

            URL website = m_fileProvider.getFileUrl();
            File tempFile = File.createTempFile(m_fileProvider.getFileName(), m_fileProvider.getFileExt());
            FileUtils.copyURLToFile(website, tempFile, 3000, 3000);

            if (file.exists() && !file.delete())
                throw new IOException("Could not delete " + id);

            if (!tempFile.renameTo(file))
                throw new IOException("Could not move temp download file");
            return true;
        }
    }
}
