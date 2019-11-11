package com.yht.frame.widgets.gridview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.R;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;

import java.util.ArrayList;

/**
 * @author dundun
 * @date 16/10/21
 */
public class AutoGridView extends RelativeLayout {
    private GridView gridView;
    private int gvWidth, gvHeight, imgWidth, imgSpace, imgPadding, numColumns = 4;
    private ArrayList<NormImage> images;
    private Context context;
    /**
     * 是否显示添加按钮及删除按钮
     */
    private boolean isAdd;
    /**
     * 是否显示数量统计
     */
    private boolean showNum;
    /**
     * 最大数
     */
    private int maxTotal;

    public AutoGridView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AutoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        imgWidth = getResources().getDimensionPixelSize(R.dimen.image_width);
        imgSpace = getResources().getDimensionPixelSize(R.dimen.image_space);
        imgPadding = getResources().getDimensionPixelSize(R.dimen.image_padding);
        gvWidth = numColumns * imgWidth + (numColumns + 1) * imgSpace;
        images = new ArrayList<>();
        gridView = new CustomGridView(getContext());
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setNumColumns(numColumns);
        gridView.setPadding(imgPadding, 0, imgPadding, 0);
        gridView.setVerticalSpacing(imgSpace);
        updateImg(images, true);
        addView(gridView);
    }

    public void initGridView() {
        if (gridView != null) {
            int rows;
            int size = images.size();
            if (size <= numColumns) {
                rows = 1;
            }
            else if (size <= (numColumns + numColumns)) {
                rows = 2;
            }
            else {
                rows = 3;
            }
            gvHeight = rows * imgWidth + (rows + 1) * imgSpace;
            gridView.setLayoutParams(new LayoutParams(gvWidth, gvHeight));
            gridView.invalidate();
        }
    }

    public void setShowNum(boolean showNum, int maxTotal) {
        this.maxTotal = maxTotal;
        this.showNum = showNum;
    }

    public void updateImg(ArrayList<NormImage> bitmaps, boolean isAdd) {
        this.isAdd = isAdd;
        images.clear();
        images.addAll(bitmaps);
        if (bitmaps.size() < BaseData.BASE_IMAGE_SIZE_MAX && isAdd) {
            images.add(new NormImage());
        }
        initGridView();
        ImgAdapter adapter = new ImgAdapter();
        gridView.setAdapter(adapter);
        gridView.invalidate();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        gridView.setOnItemLongClickListener(listener);
    }

    private class ImgAdapter extends BaseAdapter {
        private ViewHolder holder;

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_add_image_two, parent, false);
                holder.imageView = convertView.findViewById(R.id.iv_upload);
                holder.ivDelete = convertView.findViewById(R.id.iv_delete);
                holder.tvNum = convertView.findViewById(R.id.tv_num);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            String url = images.get(position).getImageUrl();
            if (!TextUtils.isEmpty(url) && isAdd) {
                holder.ivDelete.setVisibility(VISIBLE);
                holder.ivDelete.setOnClickListener(v -> {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                });
            }
            else {
                holder.ivDelete.setVisibility(GONE);
            }
            if (showNum) {
                holder.tvNum.setVisibility(VISIBLE);
                if (images.size() == 1) {
                    holder.tvNum.setText(R.string.txt_add_image_hint);
                }
                else {
                    holder.tvNum.setText(
                            String.format(context.getString(R.string.txt_percent_num), images.size() - 1, maxTotal));
                }
            }
            else {
                holder.tvNum.setVisibility(GONE);
            }
            Glide.with(context)
                 .load(url)
                 .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(context, 4)))
                 .into(holder.imageView);
            return convertView;
        }
    }

    private class ViewHolder {
        private ImageView imageView, ivDelete;
        private TextView tvNum;
    }

    public interface OnDeleteClickListener {
        /**
         * 删除
         */
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;
}
