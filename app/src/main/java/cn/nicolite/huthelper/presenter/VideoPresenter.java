package cn.nicolite.huthelper.presenter;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.Video;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.VideoActivity;
import cn.nicolite.huthelper.view.iview.IVideoView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-12-4.
 */

public class VideoPresenter extends BasePresenter<IVideoView, VideoActivity> {
    public VideoPresenter(IVideoView view, VideoActivity activity) {
        super(view, activity);
    }

    public void showVideoList(final boolean isManual) {
        APIUtils.INSTANCE
                .getVideoAPI()
                .getVideoData()
                .compose(getActivity().<Video>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Video>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null && !isManual) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(Video video) {
                        if (getView() != null) {
                            getView().closeLoading();
                            for (Video.LinksBean videoItem : video.getLinks()) {
                                videoItem.setImg(video.get_$480P() + videoItem.getImg());
                            }
                            getView().showVideoList(video);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
