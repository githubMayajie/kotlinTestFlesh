package com.jiubao.componentes.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jiubao.testflesh.R;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

public class VideoFragment extends Fragment {
    private String videoUrl = "http://sp.9ky.cc/vlook.php?id=YklkPTQ5NDcyMjY=";
    private MediaController mediaController;
    private VideoView videoView;
    private int lastPosition = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.cc_fragment_video,container,false)// super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaController = new MediaController(getContext());
        videoView = view.findViewById(R.id.video);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(videoUrl);
        videoView.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.pause();
        lastPosition = videoView.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
        videoView.seekTo(lastPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}




























