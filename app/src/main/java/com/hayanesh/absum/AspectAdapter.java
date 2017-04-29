package com.hayanesh.absum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.zagum.expandicon.ExpandIconView;

import java.util.List;

/**
 * Created by Hayanesh on 29-Mar-17.
 */

public class AspectAdapter extends RecyclerView.Adapter<AspectAdapter.MyViewHolder> {

    public Context mContext;
    public String a_name,a_score,a_summary;
    private List<Aspects> asp;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView aspect_name,aspect_score,aspect_summ;
        public LinearLayout expandable_linear;
        public LinearLayout top_linear;
        public ExpandIconView expandIconView;
        public DonutProgress donutProgress;
        public TextView pos,neg;

        public MyViewHolder(View view)
        {
            super(view);
            aspect_name = (TextView)view.findViewById(R.id.app_name);
            //aspect_score = (TextView)view.findViewById(R.id.wattage);
            donutProgress = (DonutProgress) view.findViewById(R.id.arc_progress_aspect);
            aspect_summ = (TextView)view.findViewById(R.id.aspect_summary);
            expandable_linear = (LinearLayout)view.findViewById(R.id.app_below);
            expandIconView =(ExpandIconView)view.findViewById(R.id.expand_icon);
            top_linear =(LinearLayout)view.findViewById(R.id.linearTop);
            pos = (TextView)view.findViewById(R.id.a_pos);
            neg = (TextView)view.findViewById(R.id.a_neg);
        }

    }

    public AspectAdapter(Context context,List<Aspects> asp) {
        this.mContext = context;
        this.asp = asp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aspect1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Aspects ass = asp.get(position);
        holder.aspect_name.setText(ass.getName());
        holder.donutProgress.setProgress(Integer.valueOf(ass.getScore()));
        holder.donutProgress.setText(ass.getScore());
       // holder.aspect_score.setText(ass.getScore());
        holder.aspect_summ.setText(ass.getSummary());
        holder.pos.setText(ass.getPos());
        holder.neg.setText(ass.getNeg());
        holder.top_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // holder.expandIconView.switchState();
                if(holder.expandable_linear.getVisibility() == LinearLayout.GONE)
                {
                    holder.expandable_linear.setVisibility(LinearLayout.VISIBLE);
                    holder.expandable_linear.animate().alpha(1.0f).setDuration(400).start();
                }
                else {
                    holder.expandable_linear.animate().alpha(0.0f).setDuration(400).start();
                    holder.expandable_linear.setVisibility(LinearLayout.GONE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return asp.size();
    }
}
