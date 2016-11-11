package io.reist.sandbox.repos.presenter;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.repos.ReposModule;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.repos.view.RepoEditView;
import io.reist.sandbox.repos.view.RepoListView;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by m039 on 11/27/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class ReposPresentersTest extends RobolectricTestCase {

    @Inject
    RepoListPresenter mRepoListPresenter;

    @Inject
    RepoEditPresenter mRepoEditPresenter;

    @Before
    public void initComponents() {
        DaggerReposPresentersTest_TestComponent
                .builder()
                .reposModule(new TestReposModule())
                .sandboxModule(new SandboxModule(RuntimeEnvironment.application))
                .build()
                .inject(this);
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {

        void inject(ReposPresentersTest reposPresentersTest);

    }

    public static class TestReposModule extends ReposModule {

        @Override
        protected RepoService repoService(@Named(SandboxModule.LOCAL_SERVICE) RepoService local,
                                          @Named(SandboxModule.REMOTE_SERVICE) RepoService remote) {
            RepoService mockedRepoService = mock(RepoService.class);

            List<Repo> repos = new ArrayList<>();

            User owner = new User();

            owner.id = 1L;
            owner.name = "Vasya";

            Repo repo1 = new Repo();

            repo1.id = 2L;
            repo1.owner = owner;
            repo1.name = "Cracky";

            Repo repo2 = new Repo();

            repo2.id = 3L;
            repo2.owner = owner;
            repo2.name = "Vakio";

            repos.add(repo1);
            repos.add(repo2);

            doReturn(Observable.just(new SandboxResponse<>(repos)))
                    .when(mockedRepoService)
                    .list();

            doReturn(Observable.just(new SandboxResponse<>(repo1)))
                    .when(mockedRepoService)
                    .byId(any());

            return mockedRepoService;
        }

    }

    @Test
    public void testRepoListPresenter() throws InterruptedException {
        assertThat(mRepoListPresenter).isNotNull();

        mRepoListPresenter.setView(mock(RepoListView.class));

        Thread.sleep(1000);

        assertThat(mRepoListPresenter.isDataLoaded()).isTrue();
    }

    @Test
    public void testRepoEditPresenter() throws InterruptedException {
        assertThat(mRepoEditPresenter).isNotNull();

        RepoEditView mockedRepoEditView = mock(RepoEditView.class);

        when(mockedRepoEditView.getRepoId()).thenReturn(1L);

        mRepoEditPresenter.setView(mockedRepoEditView);

        Thread.sleep(1000);

        assertThat(mRepoEditPresenter.isDataLoaded()).isTrue();
    }

}
