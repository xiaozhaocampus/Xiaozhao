package com.campus.xiaozhao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.ApplicationInfo;

public class MeFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_me, container, false);
		TextView version = (TextView)view.findViewById(R.id.more_version_check);
		String old = getResources().getString(R.string.version_check);
		String currentVersion = ApplicationInfo.getVersion(getActivity());
		version.setText(String.format(old, currentVersion));
		return view;
	}
}
