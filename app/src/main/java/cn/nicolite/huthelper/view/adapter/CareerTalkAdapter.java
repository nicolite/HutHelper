package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.CareerTalk;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkAdapter extends RecyclerView.Adapter<CareerTalkAdapter.CareerTalkViewHolder> {

    private Context context;
    private List<CareerTalk> careerTalkList;

    public CareerTalkAdapter(Context context, List<CareerTalk> careerTalkList) {
        this.context = context;
        this.careerTalkList = careerTalkList;
    }

    @NonNull
    @Override
    public CareerTalkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_careertalk_list, parent, false);
        return new CareerTalkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CareerTalkViewHolder holder, int position) {
        CareerTalk careerTalk = careerTalkList.get(position);

        Glide
                .with(context)
                .load(careerTalk.getLogoUrl())
                .skipMemoryCache(true)
                .crossFade()
                .into(holder.careertalkLog);

        holder.careertalkCompany.setText(careerTalk.getCompany());
        holder.careertalkAddress.setText(String.valueOf(careerTalk.getUniversityShortName() + "   " + careerTalk.getAddress()));
        holder.careertalkHoldtime.setText(careerTalk.getHoldtime());

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(careerTalkList) ? 0 : careerTalkList.size();
    }

    static class CareerTalkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.careertalk_log)
        ImageView careertalkLog;
        @BindView(R.id.careertalk_company)
        TextView careertalkCompany;
        @BindView(R.id.careertalk_address)
        TextView careertalkAddress;
        @BindView(R.id.careertalk_holdtime)
        TextView careertalkHoldtime;
        @BindView(R.id.rootView)
        LinearLayout rootView;

        public CareerTalkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
