package cn.nicolite.huthelper.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.FreshmanGuide;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.FreshmanGuideActivity;
import cn.nicolite.huthelper.view.iview.IFreshmanGuideView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-13.
 */

public class FreshmanGuidePresenter extends BasePresenter<IFreshmanGuideView, FreshmanGuideActivity> {
    public FreshmanGuidePresenter(IFreshmanGuideView view, FreshmanGuideActivity activity) {
        super(view, activity);
    }

    public void showGuideList() {
        Observable.create(new ObservableOnSubscribe<List<FreshmanGuide>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FreshmanGuide>> e) throws Exception {
                Document document = Jsoup.connect(Constants.FRESHMANGUIDE_URL)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/50.0.2661.102 Safari/537.36")
                        .get();
                Elements content = document.getElementsByClass("content");
                content.select("p").first().remove();
                Elements a = content.select("a");
                List<FreshmanGuide> dataList = new ArrayList<FreshmanGuide>();
                for (Element element : a) {
                    if (element.text().equals("校内网全篇pdf下载") || element.text().equals("<< 返回首页")) {
                        continue;
                    }
                    FreshmanGuide freshmanHelpData = new FreshmanGuide();
                    freshmanHelpData.setTitle(element.text());
                    freshmanHelpData.setUrl(element.attr("abs:href"));
                    dataList.add(freshmanHelpData);
                }
                e.onNext(dataList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FreshmanGuide>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(List<FreshmanGuide> freshmanGuides) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showGuideList(freshmanGuides);
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
