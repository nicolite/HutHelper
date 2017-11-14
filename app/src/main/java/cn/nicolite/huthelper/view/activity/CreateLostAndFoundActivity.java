package cn.nicolite.huthelper.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.presenter.CreateLostAndFoundPresenter;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ImageAdapter;
import cn.nicolite.huthelper.view.iview.ICreateLostAndFoundView;
import cn.nicolite.huthelper.view.widget.CommonDialog;

/**
 * Created by nicolite on 17-11-12.
 */

public class CreateLostAndFoundActivity extends BaseActivity implements ICreateLostAndFoundView {
    @BindView(R.id.toolbar_select_1)
    RadioButton toolbarSelect1;
    @BindView(R.id.toolbar_select_2)
    RadioButton toolbarSelect2;
    @BindView(R.id.et_text_content)
    EditText etTextContent;
    @BindView(R.id.add_pic)
    ImageView addPic;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_thing)
    TextView tvThing;
    @BindView(R.id.ed_thing)
    EditText edThing;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ed_time)
    EditText edTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ed_location)
    EditText edLocation;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private static final int FOUND = 1;
    private static final int LOST = 2;
    private int type = FOUND;
    private final int REQUEST_CODE_CHOOSE = 111;
    private List<Uri> uriList = new ArrayList<>();
    private ImageAdapter adapter;
    private CreateLostAndFoundPresenter createLostAndFoundPresenter;
    private final int[] bg = {R.color.bg_list_1, R.color.bg_list_2, R.color.bg_list_3, R.color.bg_list_4};

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
        return R.layout.activity_create_lost_and_found;
    }

    @Override
    protected void doBusiness() {
        toolbarSelect1.setText("招领");
        toolbarSelect2.setText("寻物");
        rootView.setBackgroundColor(getResources().getColor(bg[(int) (Math.random() * 100 % 4)]));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
        adapter = new ImageAdapter(context, uriList, ImageAdapter.URI);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, long itemId) {
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认删除这张图片？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                commonDialog.dismiss();
                                uriList.remove(uriList.get(position));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("不了", null)
                        .show();

            }
        });

        createLostAndFoundPresenter = new CreateLostAndFoundPresenter(this, this);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_select_1, R.id.toolbar_select_2, R.id.toolbar_ok,
            R.id.ed_time, R.id.add_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_select_1:
                type = FOUND;
                rootView.setBackgroundColor(getResources().getColor(bg[(int) (Math.random() * 100 % 4)]));
                tvThing.setText("拾到物品");
                tvTime.setText("拾到时间");
                tvLocation.setText("拾到地点");
                edThing.setHint("请输入拾到物品名称");
                edTime.setHint("请选择拾到时间");
                edLocation.setHint("请输入拾到地点");
                break;
            case R.id.toolbar_select_2:
                type = LOST;
                rootView.setBackgroundColor(getResources().getColor(bg[(int) (Math.random() * 100 % 4)]));
                tvThing.setText("丢失物品");
                tvTime.setText("丢失时间");
                tvLocation.setText("丢失地点");
                edThing.setHint("请输入丢失物品名称");
                edTime.setHint("请选择丢失时间");
                edLocation.setHint("请输入丢失地点");
                break;
            case R.id.toolbar_ok:
                KeyBoardUtils.hideSoftInput(context, getWindow());
                if (!ButtonUtils.isFastDoubleClick()) {
                    if (!ListUtils.isEmpty(uriList)) {
                        createLostAndFoundPresenter.createGoods(activity, uriList);
                    } else {
                        showMessage("至少选择一张图片！");
                    }
                }
                break;
            case R.id.ed_time:
                final Calendar calendar = Calendar.getInstance(Locale.CHINA);
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        final String date = i + "-" + i1 + "-" + i2;
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                edTime.setText(String.valueOf(date + " " + i + ":" + i1));
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                                .show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.add_pic:
                createLostAndFoundPresenter.selectImages();
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
    public void selectImages() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(4 - uriList.size())
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.80f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void uploadLostAndFoundInfo(String hidden) {
        createLostAndFoundPresenter.uploadLostAndFoundInfo(edThing.getText().toString(),
                tvLocation.getText().toString(), tvTime.getText().toString(), tvPhone.getText().toString(),
                hidden, edPhone.getText().toString(), type);

    }

    @Override
    public void publishSuccess() {
        //finish();
        new CommonDialog(context)
                .setMessage("发布成功！")
                .setPositiveButton("确认", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            uriList.addAll(Matisse.obtainResult(data));
            adapter.notifyDataSetChanged();
        }
    }
}
