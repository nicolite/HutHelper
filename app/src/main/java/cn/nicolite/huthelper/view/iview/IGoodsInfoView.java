package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.GoodsItem;

/**
 * Created by nicolite on 17-11-9.
 */

public interface IGoodsInfoView extends IBaseView {
    void showGoodsInfo(GoodsItem goodsItem);
    void deleteSuccess();
}
