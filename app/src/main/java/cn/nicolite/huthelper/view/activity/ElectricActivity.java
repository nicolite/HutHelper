package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.bean.Electric;
import cn.nicolite.huthelper.presenter.ElectricPresenter;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.iview.IElectricView;
import cn.nicolite.huthelper.view.customView.BarListView;
import cn.nicolite.huthelper.view.customView.ElectricDialog;

/**
 * 电费查询界面
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
        electricPresenter.showLouHao();
        electricPresenter.showWeather();
        electricPresenter.showVoteSummary();
    }

    @OnClick({R.id.toolbar_back, R.id.btn_electric_ok, R.id.radio_electric_open, R.id.radio_electric_unopen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.btn_electric_ok:
                KeyBoardUtils.hideSoftInput(context, getWindow());
                electricPresenter.showElectricData(etElectricLou.getText().toString(), etElectricHao.getText().toString());
                break;
            case R.id.radio_electric_open:
                electricPresenter.showVoteDialog(ElectricPresenter.YES);
                break;
            case R.id.radio_electric_unopen:
                electricPresenter.showVoteDialog(ElectricPresenter.NO);
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
    public void showLouHao(String lou, String hao) {
        etElectricLou.setText(lou);
        etElectricHao.setText(hao);
    }

    @Override
    public void showElectric(Electric electric) {
        ElectricDialog electricDialog = new ElectricDialog(context);

        electricDialog
                .setData(electric.getAmmeter(), electric.getBalance())
                .setOkButton("确认", null)
                .show();
    }

    @Override
    public void showWeather(String city, String tmp, String content) {
        tvWdLocation.setText(String.valueOf(city + "|" + content));
        tvWdTemp.setText(String.valueOf(tmp + "℃"));
    }

    @Override
    public void showVoteSummary(String yes, String no, String opt) {

        if (!TextUtils.isEmpty(opt)) {

            radioElectricOpen.setEnabled(false);
            radioElectricUnopen.setEnabled(false);

            if (opt.equals("1")) {
                radioElectricOpen.setChecked(true);
            } else if (opt.equals("2")) {
                radioElectricUnopen.setChecked(true);
            }

            if (!TextUtils.isEmpty(yes) && !TextUtils.isEmpty(no)) {
                List<BarListView.Bar> barList = new ArrayList<>();
                BarListView.Bar bar = elebarElectric.new Bar();
                bar.setName("开了");
                bar.setNum(Integer.parseInt(yes));
                BarListView.Bar bar2 = elebarElectric.new Bar();
                bar2.setName("没开");
                bar2.setNum(Integer.parseInt(no));
                barList.add(bar);
                barList.add(bar2);

                elebarElectric.setBars(barList);
                elebarElectric.setVisibility(View.VISIBLE);
            }
        }

    }

}
