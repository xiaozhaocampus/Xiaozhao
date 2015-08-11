package com.campus.xiaozhao.basic.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.campus.xiaozhao.activity.CampusDetailActivity;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;

public class ActivityUtils {
	private ActivityUtils(){
		
	}
	public static void showCampusDetailActivity(Context context, CampusInfoItemData data) {
        Intent intent = new Intent(context, CampusDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detail_data", data);
        intent.putExtras(bundle);
        context.startActivity(intent);
	}
}
