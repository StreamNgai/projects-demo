package dev.ngai.fantastic.business.main.collect;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.Collect;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.UserPrivate;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.RefreshCollectEvent;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;
import dev.ngai.fantastic.data.source.CollectDataSource;
import dev.ngai.fantastic.data.source.CollectRepository;
import dev.ngai.fantastic.data.source.UserPrivateDataSource;
import dev.ngai.fantastic.data.source.UserPrivateRepository;
import dev.ngai.fantastic.data.source.local.CollectLocalDataSource;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.UserPrivateLocalDataSource;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.ImginfoUtil;

import static com.google.common.base.Preconditions.checkNotNull;


public class CollectPresenter implements MainContract.CollectPresenter {

    private MainContract.CollectView mCollectView;
    private CollectRepository mCollectRepository;
    private UserPrivateDataSource mUserPrivateDS;

    public CollectPresenter(MainContract.CollectView collectView) {
        this.mCollectView = checkNotNull(collectView);
        this.mCollectRepository = checkNotNull(CollectRepository.getInstance());
        this.mUserPrivateDS = checkNotNull(UserPrivateRepository.getInstance());

        collectView.setPresenter(this);

    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        Logc.d("CollectPresenter", "register !R");
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
        Logc.d("CollectPresenter", "unregister !");
    }

    @Override
    public void loadCollectData() {
        mCollectRepository.getCollectList(new CollectDataSource.LoadCollectListCallback() {

            @Override
            public void onCollectListLoaded(List<Discover> collectsNotPay, List<Discover> collectsHasPay) {
                mCollectView.displayCollectDatas(collectsNotPay, collectsHasPay);
            }

            @Override
            public void onDataNotAvailable() {
                mCollectView.onDataNotAvailable();
            }
        });
    }

    @Override
    public void doDeleteCollects(boolean hasPay, List<Discover> stayDiscover, List<Discover> deleteDiscover) {
        try {

            // 删了收藏列表
            ArrayList<Long> deleteCollectIds = new ArrayList<>();
            for (Discover discover : deleteDiscover) {
                deleteCollectIds.add(Long.valueOf(discover.getTag()));
            }
            mCollectRepository.deleteCollectByIds(deleteCollectIds);

            // 更新收藏IDs
            ArrayList<Integer> allCollectDiscoverIds = new ArrayList<>();
            ArrayList<Integer> allHasPayCollectDiscoverIds = new ArrayList<>();
            List<Collect> collectList = DaoHelper.getDaoSession().getCollectDao().loadAll();
            Gson gson = new Gson();
            if (collectList != null && !collectList.isEmpty()) {
                for (Collect collect : collectList) {
                    Discover tDis = gson.fromJson(collect.getData(), Discover.class);
                    allCollectDiscoverIds.add(tDis.getId());
                    if (mUserPrivateDS.hasPayAlbum(tDis.getId())) {
                        allHasPayCollectDiscoverIds.add(tDis.getId());
                    }
                }
            }

            String collectIdsJson = new Gson().toJson(allCollectDiscoverIds);
            Logc.d("CollectPresenter", "全部收藏Ids : " + collectIdsJson);
            String hasPayCollectIdsJson = new Gson().toJson(allHasPayCollectDiscoverIds);
            Logc.d("CollectPresenter", "已经兑换Ids : " + hasPayCollectIdsJson);

            DaoHelper.getDaoSession().getUserPrivateDao().deleteAll();
            UserPrivate userPrivate = new UserPrivate();
            userPrivate.setCollectIds(collectIdsJson);
            userPrivate.setPayAlbumIds(hasPayCollectIdsJson);
            DaoHelper.getDaoSession().getUserPrivateDao().insertOrReplace(userPrivate);

            UserPrivateLocalDataSource.getInstance().refreshPayAlumIds();
            CollectLocalDataSource.getInstance().refreshLocalHasCollectIds();

            EventBus.getDefault().post(new RefreshCollectEvent());
            EventBus.getDefault().post(new RefreshDiscoverEvent(-1));
            EventBus.getDefault().post(new RefreshAccountEvent());

        } catch (Exception e) {

        }
    }

    @Override
    public void doDownloadCollects(List<Discover> downloadDiscovers) {
        EventBus.getDefault().post(new LoadingEvent(true,"专辑下载!","请稍后片刻..."));
        File pictureFolder = Environment.getExternalStorageDirectory();

        List<String> allDisImgUrls = new ArrayList<>();
        for (Discover discover : downloadDiscovers) {
            File picDir = new File(pictureFolder, "Fantastic/"+discover.getDesc());
            if (!picDir.exists()) {
                if (picDir.mkdirs()) {
                    downloadDiscover(allDisImgUrls, picDir, discover);
                } else {
                    Logc.d("doDownloadCollects", "创建目录失败! 专辑 = " + discover.getDesc());
                }
            } else {
                Logc.d("doDownloadCollects", "已经下载过些专辑!");
            }
        }
        if(allDisImgUrls.isEmpty()){
            EventBus.getDefault().post(new LoadingEvent(false, "", ""));
        }
    }

    @Override
    public List<Imginfo> parsingDetails(String details) {
        return ImginfoUtil.parsingDetails(details);
    }

    private void downloadDiscover(final List<String> allDisImgUrls, final File picDir, Discover discover) {

        if (!TextUtils.isEmpty(discover.getDesc())) {
            List<String> imgUrls = new Gson().fromJson(discover.getDetails(), new TypeToken<List<String>>() {
            }.getType());
            allDisImgUrls.addAll(imgUrls);
            final int[] i = {0};
            for (final String imgUrl : imgUrls) {
                Glide.with(Appfantastic.getContext()).load(imgUrl).downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        String typeStr = imgUrl.substring(imgUrl.lastIndexOf("."), imgUrl.length());
                        Logc.d("doDownloadCollects", "链接：" + imgUrl + " ; " + typeStr + " ; picDir = " + picDir);
                        ++i[0];
                        File destFile = new File(picDir, i[0] + typeStr);
                        try {
                            Files.copy(resource, destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Logc.d("doDownloadCollects", "下载失败：" + imgUrl);
                        }

                        if (allDisImgUrls.size() == 1) {
                            // 最后通知图库更新
//                            Appfantastic.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                                    Uri.fromFile(new File(destFile.getPath()))));
//                            SharedPres.putString(PrefsKey.DownloadFilePath,destFile.getAbsolutePath());
                            mCollectView.showSkipPictureDialog();
                            EventBus.getDefault().post(new LoadingEvent(false, "", ""));
                        } else {
                            allDisImgUrls.remove(imgUrl);
                        }
                    }
                });
            }
        }

    }


    // event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshCollectEvent(RefreshCollectEvent event) {/* Do something */
        Logc.d("CollectPresenter", "onRefreshCollectEvent !");
        loadCollectData();
    }
}
