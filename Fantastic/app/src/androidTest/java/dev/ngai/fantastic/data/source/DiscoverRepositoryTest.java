package dev.ngai.fantastic.data.source;

import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.local.DaoHelper;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

/**
 * @author Weihl
 * @since 2017/7/3
 * Des:
 */
public class DiscoverRepositoryTest {


    DiscoverRepository mDiscoverRepository;

    @Before
    public void setUp() throws Exception {
        DaoHelper.initContext(InstrumentationRegistry.getTargetContext());
        mDiscoverRepository = DiscoverRepository.getInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getDiscoverList() throws Exception {
        mDiscoverRepository.getDiscoverList("qingchun", Constant.Load.New, 0, 3, new DiscoverDataSource.LoadDiscoverListCallback() {
            @Override
            public void onDiscoverListLoaded(List<Discover> discover) {
                checkNotNull(discover);
                Logc.d("DiscoverRepositoryTest", discover.toString());
            }

            @Override
            public void onDataNotAvailable() {
                Logc.d("DiscoverRepositoryTest", "onDataNotAvailable");
            }
        });
    }

    @Test
    public void updateDiscover() throws Exception {

    }

}