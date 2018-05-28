package cn.nicolite.huthelper.presenter;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.UploadImages;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.EncryptUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.CreateGoodsActivity;
import cn.nicolite.huthelper.view.iview.ICreateGoodsView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by nicolite on 17-11-11.
 */

public class CreateGoodsPresenter extends BasePresenter<ICreateGoodsView, CreateGoodsActivity> {
    public CreateGoodsPresenter(ICreateGoodsView view, CreateGoodsActivity activity) {
        super(view, activity);
    }

    public void uploadGoodsInfo(int type, String title, String content, String price, int attr, String phone, String address, String hidden) {

        if (getView() != null) {
            if (TextUtils.isEmpty(phone)) {
                getView().showMessage("联系方式必须填写");
                return;
            } else if (TextUtils.isEmpty(String.valueOf(attr))) {
                getView().showMessage("请选择商品成色");
                return;
            } else if (TextUtils.isEmpty(address)) {
                getView().showMessage("请填写发布区域");
                return;
            }
        }

        APIUtils.INSTANCE
                .getMarketAPI()
                .createGoods(configure.getStudentKH(), configure.getAppRememberCode(),
                        title, content, price, attr, phone, address, type, hidden)
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
                                getView().showMessage("发布成功！");
                                getView().publishSuccess();
                            } else {
                                getView().showMessage("发布失败，" + stringHttpResult.getCode());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("发布失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private StringBuilder stringBuilder = new StringBuilder();
    private AtomicInteger uploadCount = new AtomicInteger(0);


    /**
     * @param bitmap
     * @param i      现在上传的是第几个
     * @param count  总共需要上传的图片个数
     */
    public void uploadImages(Bitmap bitmap, final int count, final int i) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        String date = simpleDateFormat.format(new Date());
        String env = EncryptUtils.SHA1(configure.getStudentKH() + configure.getAppRememberCode() + date);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("img/jpeg"), bytes);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", "01.jpg", requestBody);

        if (getView() != null) {
            getView().uploadProgress(String.valueOf("正在上传图片"));
        }

        APIUtils.INSTANCE
                .getUploadAPI()
                .uploadImages(configure.getStudentKH(), configure.getAppRememberCode(), env, 1, file)
                .compose(getActivity().<UploadImages>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadImages>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(UploadImages uploadImages) {
                        if (getView() != null) {
                            if (uploadImages.getCode() == 200) {
                                uploadCount.incrementAndGet();
                                stringBuilder.append("//");
                                stringBuilder.append(uploadImages.getData());
                                if (getView() != null && uploadCount.get() == count) {
                                    String string = stringBuilder.toString();
                                    stringBuilder.delete(0, stringBuilder.length());
                                    if (!TextUtils.isEmpty(string)) {
                                        getView().uploadGoodsInfo(string);
                                    } else {
                                        stringBuilder.delete(0, stringBuilder.length());
                                        uploadCount.set(0);
                                        getView().uploadFailure("获取上传图片信息失败！");
                                    }
                                }
                            } else {
                                stringBuilder.delete(0, stringBuilder.length());
                                uploadCount.set(0);
                                getView().uploadFailure(String.valueOf("上传图片失败！"));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null) {
                            stringBuilder.delete(0, stringBuilder.length());
                            uploadCount.set(0);
                            getView().uploadFailure("上传失败！");
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void selectImages() {

        AndPermission
                .with(getActivity())
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (getView() != null) {
                            if (requestCode == 100) {
                                getView().selectImages();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (getView() != null) {
                            getView().showMessage("获取权限失败，请授予文件读写权限！");
                        }
                    }
                })
                .start();
    }


    List<File> fileList = new ArrayList<>();

    public void createGoods(Activity activity, final List<Uri> uriList) {
        if (ListUtils.isEmpty(uriList)) {
            if (getView() != null) {
                getView().showMessage("未选择图片！");
            }
            return;
        }

        if (getView() != null) {
            getView().showMessage("正在发布，请勿关闭页面！");
        }

        for (int i = 0; i < uriList.size(); i++) {

            if (getView() != null) {
                getView().uploadProgress(String.valueOf("正在压缩图片！"));
            }

            Luban
                    .with(activity)
                    .load(CommUtil.uri2File(activity, uriList.get(i)))
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            fileList.add(file);
                            if (!ListUtils.isEmpty(fileList) && fileList.size() == uriList.size()) {
                                for (int i = 0; i < fileList.size(); i++) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(fileList.get(i).getPath());
                                    uploadImages(bitmap, fileList.size(), i + 1);
                                    bitmap.recycle();
                                }
                                fileList.clear();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (getView() != null) {
                                getView().uploadFailure("压缩图片出现异常！");
                                getView().showMessage("压缩失败，" + ExceptionEngine.handleException(e).getMsg());
                            }
                        }
                    }).launch();
        }

    }
}
