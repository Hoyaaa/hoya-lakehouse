package kr.ac.nsu.hakbokgs.main.advertising;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kr.ac.nsu.hakbokgs.R;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> {
    private List<Advertisement> adList;
    private final Context context;

    public AdAdapter(List<Advertisement> adList, Context context) {
        this.adList = adList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Advertisement ad = adList.get(position);
        String title = ad.getTitle();
        String websiteUrl = ad.getUrl();
        String expiration = ad.getExpiration().toDate().toString();
        String imageUrl = ad.getImageUrl();

        holder.titleTextView.setText(title);
        holder.expirationTextView.setText(expiration);

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        setClickListener(holder.imageView, websiteUrl);
    }

    private void setClickListener(View view, String websiteUrl) {
        view.setOnClickListener(v -> {
            if (websiteUrl != null && !websiteUrl.isEmpty()) {
                if (websiteUrl.startsWith("http://") || websiteUrl.startsWith("https://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("Intent Error", "Unable to open the URL: " + websiteUrl);
                        Toast.makeText(context, "유효하지 않은 URL입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "URL이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public void updateList(List<Advertisement> newList) {
        adList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView expirationTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.ad_title);
            expirationTextView = itemView.findViewById(R.id.ad_expiration);
            imageView = itemView.findViewById(R.id.ad_image);
        }
    }
}
