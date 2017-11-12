package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.GoodsItem;
import cn.nicolite.huthelper.model.bean.GoodsResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.GoodsInfoActivity;
import cn.nicolite.huthelper.view.iview.IGoodsInfoView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-9.
 */

public class GoodsInfoPresenter extends BasePresenter<IGoodsInfoView, GoodsInfoActivity> {
    public GoodsInfoPresenter(IGoodsInfoView view, GoodsInfoActivity activity) {
        super(view, activity);
    }

    public void showGoodsInfo(String goodsId) {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();

        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        APIUtils
                .getMarketAPI()
                .getGoodsInfo(user.getStudentKH(), configure.getAppRememberCode(), goodsId)
                .compose(getActivity().<GoodsResult<GoodsItem>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsResult<GoodsItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(GoodsResult<GoodsItem> goodsItemGoodsResult) {
                        if (getView() != null) {
                            if (goodsItemGoodsResult.getCode() == 200) {
                                if (goodsItemGoodsResult.getData() != null) {
                                    getView().showGoodsInfo(goodsItemGoodsResult.getData());
                                }
                            } else {
                                getView().showMessage("从服务器获取数据失败，请检查网络！");
                            }
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

    public void deleteGoods(String goodId) {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();

        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        if (getView() != null) {
            getView().showMessage("正在删除！");
        }

        APIUtils
                .getMarketAPI()
                .deleteGoods(user.getStudentKH(), configure.getAppRememberCode(), goodId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (stringHttpResult.getCode() == 200) {
                                getView().showMessage("删除成功！");
                                getView().deleteSuccess();
                            } else {
                                getView().showMessage("删除失败，" + stringHttpResult.getCode());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("删除失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
