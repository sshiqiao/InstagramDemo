package com.start.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.utils.Utils;

import static android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;


public class IGMediaView extends ViewGroup implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener ,View.OnClickListener{


    private Context mContext;
    private GestureDetector mGestureDetector;

    private VideoView mVideoView;
    private SimpleDraweeView mImageView;
    private ImageView mIconImageView;
    private ImageView mLikeImageView;
    private ImageView mCommentImageView;
    private boolean isVideoType;

    private MediaPlayer mMediaPlayer;
    private boolean isVolumeOn;
    private boolean isStarted;
    private OnClickImageListener onClickImageListener;
    public IGMediaView(Context context) {
        super(context);
        init(context);
    }
    public IGMediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray  = context.obtainStyledAttributes(attrs,R.styleable.IGMediaView);
        isVideoType = typedArray.getBoolean(R.styleable.IGMediaView_media_type,false);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mGestureDetector = new GestureDetector(mContext, new GestureListener());
        setOnTouchListener(this);
        if(isVideoType) {
            mVideoView = new VideoView(mContext);
            mVideoView.setTag(Utils.MEDIAVIEW_VIDEO_VIEW_TAG);
            mVideoView.setOnPreparedListener(this);
            addView(mVideoView);
        }else {
            mImageView = new SimpleDraweeView(mContext);
            mImageView.setTag(Utils.MEDIAVIEW_FIRST_FRAME_IMAGE_TAG);
            mImageView.setBackgroundResource(R.color.colorWhite);
            addView(mImageView);
        }
        mIconImageView = new ImageView(mContext);
        mIconImageView.setTag(Utils.MEDIAVIEW_ICON_IMAGE_TAG);
        mIconImageView.setImageResource(isVideoType?R.drawable.ic_volume_off_white_24dp:R.drawable.ic_person_white_24dp);
        mIconImageView.setVisibility(isVideoType?View.VISIBLE:View.INVISIBLE);
        mIconImageView.setBackgroundResource(R.drawable.shape_media_bg);
        mIconImageView.setPadding(Utils.dp2px(3), Utils.dp2px(3), Utils.dp2px(3), Utils.dp2px(3));
        addView(mIconImageView);

        mLikeImageView = new ImageView(mContext);
        mLikeImageView.setTag(Utils.MEDIAVIEW_LIKE_IMAGE_TAG);
        mLikeImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        mLikeImageView.setBackgroundResource(R.drawable.shape_media_bg);
        mLikeImageView.setPadding(Utils.dp2px(3), Utils.dp2px(3), Utils.dp2px(3), Utils.dp2px(3));
        mLikeImageView.setOnClickListener(this);
        addView(mLikeImageView);

        mCommentImageView = new ImageView(mContext);
        mCommentImageView.setTag(Utils.MEDIAVIEW_COMMENT_IMAGE_TAG);
        mCommentImageView.setImageResource(R.drawable.ic_comment_white_24dp);
        mCommentImageView.setBackgroundResource(R.drawable.shape_media_bg);
        mCommentImageView.setPadding(Utils.dp2px(4), Utils.dp2px(4), Utils.dp2px(4), Utils.dp2px(4));
        mCommentImageView.setOnClickListener(this);
        addView(mCommentImageView);

    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = this.getChildAt(i);
            switch ((int) child.getTag()) {
                case Utils.MEDIAVIEW_VIDEO_VIEW_TAG:
                case Utils.MEDIAVIEW_FIRST_FRAME_IMAGE_TAG:
                    child.layout(l, t, r, b);
                    break;
                case Utils.MEDIAVIEW_ICON_IMAGE_TAG:
                    child.layout(l + Utils.dp2px(15), b - Utils.dp2px(15) - Utils.dp2px(25), l + Utils.dp2px(15) + Utils.dp2px(25), b - Utils.dp2px(15));
                    break;
                case Utils.MEDIAVIEW_LIKE_IMAGE_TAG:
                    child.layout(r - Utils.dp2px(15) - Utils.dp2px(25) - Utils.dp2px(10) - Utils.dp2px(25), b - Utils.dp2px(15) - Utils.dp2px(25), r - Utils.dp2px(15) - Utils.dp2px(10) - Utils.dp2px(25), b - Utils.dp2px(15));
                    break;
                case Utils.MEDIAVIEW_COMMENT_IMAGE_TAG:
                    child.layout(r - Utils.dp2px(15) - Utils.dp2px(25), b - Utils.dp2px(15) - Utils.dp2px(25), r - Utils.dp2px(15), b - Utils.dp2px(15));
                    break;
            }
        }
    }

    public void setVideoURI(Uri uri) {
        if(!isVideoType)
            return;
        mVideoView.setVideoURI(uri);
    }
    public void setImageURI(Uri uri) {
        mImageView.setImageURI(uri);
    }

    public void setLikeImageViewResource(int resId){
        mLikeImageView.setImageResource(resId);
    }

    public void setOnClickImageListener(OnClickImageListener onClickImageListener){
        this.onClickImageListener = onClickImageListener;
    }

    @Override
    public void onClick(View v) {
        if(onClickImageListener!=null){
            onClickImageListener.OnClickImage(v);
        }
    }

    public interface OnClickImageListener {
        void OnClickImage(View v);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(0f, 0f);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.start();
    }
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MEDIA_INFO_VIDEO_RENDERING_START:
                isStarted = true;
                break;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(isVideoType&&isStarted) {
                if (isVolumeOn) {
                    mMediaPlayer.setVolume(0f, 0f);
                    mIconImageView.setImageResource(R.drawable.ic_volume_off_white_24dp);
                    isVolumeOn = false;
                } else {
                    float currentSystemVolume = Utils.currentSystemVolume();
                    mMediaPlayer.setVolume(currentSystemVolume, currentSystemVolume);
                    mIconImageView.setImageResource(R.drawable.ic_volume_up_white_24dp);
                    isVolumeOn = true;
                }
            }
            return true;
        }
    }
}