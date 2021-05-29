/*
 * Copyright 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.odk.collect.android.backgroundwork;

import android.content.Context;

import androidx.work.WorkerParameters;

import org.javarosa.core.reference.ReferenceManager;
import org.jetbrains.annotations.NotNull;
import org.odk.collect.analytics.Analytics;
import org.odk.collect.android.formmanagement.DiskFormsSynchronizer;
import org.odk.collect.android.formmanagement.FormMetadataParser;
import org.odk.collect.android.formmanagement.FormUpdateChecker;
import org.odk.collect.android.formmanagement.ServerFormDownloader;
import org.odk.collect.android.formmanagement.ServerFormsDetailsFetcher;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.notifications.Notifier;
import org.odk.collect.android.preferences.source.SettingsProvider;
import org.odk.collect.android.storage.StoragePathProvider;
import org.odk.collect.android.storage.StorageSubdirectory;
import org.odk.collect.android.utilities.FormsDirDiskFormsSynchronizer;
import org.odk.collect.android.utilities.FormsRepositoryProvider;
import org.odk.collect.async.TaskSpec;
import org.odk.collect.async.WorkerAdapter;
import org.odk.collect.forms.FormSource;
import org.odk.collect.forms.FormsRepository;

import java.io.File;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;

public class AutoUpdateTaskSpec implements TaskSpec {

    @Inject
    FormsRepositoryProvider formsRepositoryProvider;

    @Inject
    FormSource formSource;

    @Inject
    Notifier notifier;

    @Inject
    SettingsProvider settingsProvider;

    @Inject
    StoragePathProvider storagePathProvider;

    @Inject
    @Named("FORMS")
    ChangeLock changeLock;

    @Inject
    Analytics analytics;

    @NotNull
    @Override
    public Supplier<Boolean> getTask(@NotNull Context context) {
        DaggerUtils.getComponent(context).inject(this);

        FormsRepository formsRepository = formsRepositoryProvider.get();
        DiskFormsSynchronizer diskFormsSynchronizer = new FormsDirDiskFormsSynchronizer(formsRepository);

        ServerFormsDetailsFetcher serverFormsDetailsFetcher = new ServerFormsDetailsFetcher(
                formsRepository,
                formSource,
                diskFormsSynchronizer
        );

        String formsDirPath = storagePathProvider.getOdkDirPath(StorageSubdirectory.FORMS);
        String cacheDirPath = storagePathProvider.getOdkDirPath(StorageSubdirectory.FORMS);
        ServerFormDownloader formDownloader = new ServerFormDownloader(formSource, formsRepository, new File(cacheDirPath), formsDirPath, new FormMetadataParser(ReferenceManager.instance()), analytics);

        FormUpdateChecker formUpdateChecker = new FormUpdateChecker(context, changeLock, notifier, settingsProvider);
        return () -> formUpdateChecker.checkForUpdates(serverFormsDetailsFetcher, formDownloader);
    }

    @NotNull
    @Override
    public Class<? extends WorkerAdapter> getWorkManagerAdapter() {
        return Adapter.class;
    }

    public static class Adapter extends WorkerAdapter {

        public Adapter(@NotNull Context context, @NotNull WorkerParameters workerParams) {
            super(new AutoUpdateTaskSpec(), context, workerParams);
        }
    }

}
