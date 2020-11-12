package com.example.bucketlist;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class AdViewHolder extends RecyclerView.ViewHolder {

    UnifiedNativeAdView adView;

    public UnifiedNativeAdView getAdView() {
        return adView;
    }

    public AdViewHolder(@NonNull View itemView) {
        super(itemView);
        adView = (UnifiedNativeAdView) itemView.findViewById(R.id.ad_view);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
        adView.setCallToActionView(adView.findViewById(R.id.add_call_to_action));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    }
}
