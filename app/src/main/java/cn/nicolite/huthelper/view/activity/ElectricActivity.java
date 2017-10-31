package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Electric;
import cn.nicolite.huthelper.presenter.ElectricPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.iview.IElectricView;
import cn.nicolite.huthelper.view.widget.BarListView;
import cn.nicolite.huthelper.view.widget.CommonDialog;

/**
 * Created by nicolite on 17-10-31.
 */

public class ElectricActivity extends BaseActivity implements IElectricView {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_wd_temp)
    TextView tvWdTemp;
    @BindView(R.id.tv_wd_location)
    TextView tvWdLocation;
    @BindView(R.id.et_electric_lou)
    EditText etElectricLou;
    @BindView(R.id.et_electric_hao)
    EditText etElectricHao;
    @BindView(R.id.radio_electric_open)
    RadioButton radioElectricOpen;
    @BindView(R.id.radio_electric_unopen)
    RadioButton radioElectricUnopen;
    @BindView(R.id.rl_ele_choose)
    RelativeLayout rlEleChoose;
    @BindView(R.id.elebar_electric)
    BarListView elebarElectric;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private ElectricPresenter electricPresenter;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_electric;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("电费查询");
        electricPresenter = new ElectricPresenter(this, this);
    }

    @OnClick({R.id.toolbar_back, R.id.btn_electric_ok, R.id.radio_electric_open, R.id.radio_electric_unopen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                break;
            case R.id.btn_electric_ok:
                electricPresenter.showElectricData(etElectricLou.getText().toString(), etElectricHao.getText().toString());
                break;
            case R.id.radio_electric_open:
                break;
            case R.id.radio_electric_unopen:
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showElectric(Electric electric) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog
                .setMessage("余电：  " + electric.getAmmeter() + "\n" + "余额：  " + electric.getBalance())
                .setPositiveButton("确认", null)
                .show();
    }

}
