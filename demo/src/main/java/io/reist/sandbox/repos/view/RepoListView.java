/*
 * Copyright (C) 2017 Renat Sarymsakov.
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

package io.reist.sandbox.repos.view;

import java.util.List;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.repos.presenter.RepoListPresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by defuera on 05/11/2015.
 */
public interface RepoListView extends VisumView<RepoListPresenter> {

    void showLoader(boolean show);

    void displayError(SandboxError error);

    void displayData(List<Repo> data);

    void displaySuccess();

    void onRepoUpdated(Repo r);

}
