package pw.hais.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 音频处理工具类
 * 1、初始化
 * 2、文件放在assets 文件夹
 * 3、播放 AudioHandleUtils.getInstance().playMusic("bgm_rank.ogg",true);
 * @author Single
 * @version 1.3
 */
public class AudioHandleUtils implements OnLoadCompleteListener {

    private static final boolean DEBUG = true;
    private final String TAG = "AudioHandle";

    private MediaPlayer mPlayer;
    private SoundPool mSoundPool;
    private int soundIsLoop;
    private Context mContext;

    private final boolean mNormalState = true;
    private boolean mSoundEnable = mNormalState;
    private boolean mMusicEnable = mNormalState;
    private boolean mAllEnable = mNormalState;

    /**
     * 同时最大播放音效个数
     */
    private final int MAX_STREAMS = 4;

    /**
     * 流的类型
     */
    private final int STREAM_TYPE = AudioManager.STREAM_MUSIC;

    /**
     * 采样率转化质量
     */
    private final int QUALITY = 100;

    private int leftVolume = 1;
    private int rightVolume = 1;
    private int priority = 1;

    /**
     * 0.5 - 2.0 1为正常速度
     */
    private int rate = 1;

    private HashMap<String, Integer> mSounds;
    private String bgMusic;
    private boolean bgLooper;

    private static AudioHandleUtils mUtils;

    private SharedPreferences mPreferences;

    private final String KEY_ALL_ENABLE = "AllEnable";
    private final String KEY_SOUND_ENABLE = "SoundEnable";
    private final String KEY_MUSIC_ENABLE = "MusicEnable";

    /**
     * 记录最大SoundPoolID;
     */
    private int maxSoundPoolId;

    private final int MAX_SOUND_POOL_ID = 300;

    /**
     * 获得一个单例
     * @return
     */
    public static AudioHandleUtils getInstance() {
        if (mUtils == null) {
            mUtils = new AudioHandleUtils();
        }
        return mUtils;
    }

    private AudioHandleUtils() {
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        mSoundPool.play(sampleId, leftVolume, rightVolume, priority,0, rate);
    }

    /**
     * 初始化配置必须执行
     * @param mContext
     */
    public void initConfig(Context mContext) {
        this.mContext = mContext;

        //音乐
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //音效
        mSoundPool = new SoundPool(MAX_STREAMS, STREAM_TYPE, QUALITY);
        mSoundPool.setOnLoadCompleteListener(this);
        mSounds = new HashMap<String, Integer>();

        mPreferences = mContext.getSharedPreferences(this.getClass().getName(),Context.MODE_PRIVATE);
        mAllEnable = mPreferences.getBoolean("AllEnable",mNormalState);
        mSoundEnable = mPreferences.getBoolean("SoundEnable",mNormalState);
        mMusicEnable = mPreferences.getBoolean("MusicEnable",mNormalState);
    }

    /**
     * 播放音乐
     * @param fileName 文件名 assets
     * @param isLoop   是否循环播放
     */
    public void playMusic(String fileName, boolean isLoop) {

        try {

            //清空之前的背景音乐
            bgMusic = fileName;
            bgLooper = isLoop;

            if (!mMusicEnable || !mAllEnable) {
                return;
            }

            if (mPlayer != null) {
                logMsg("播放音乐:" + fileName);
                stopMusic();
                mPlayer.reset();
                AssetFileDescriptor asdFile = mContext.getResources().getAssets().openFd(fileName);
                mPlayer.setDataSource(asdFile.getFileDescriptor(), asdFile.getStartOffset(), asdFile.getLength());
                mPlayer.setLooping(isLoop);
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logMsg("音乐播放失败:" + ex.toString());
        }
    }

    /**
     * 停止播放音乐
     */
    public void stopMusic() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    /**
     * 暂停播放音乐
     */
    public void pauseMusic() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    /**
     * 回收
     */
    public void recycle() {
        releaseMusic();
        releaseSoundPool();
    }

    /**
     * 释放音乐
     */
    public void releaseMusic(){
        if(mPlayer!=null){
            if(mPlayer.isPlaying()){
                mPlayer.stop();
            }

            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 设置是否播放音效
     *
     * @param mSoundEnable true 表示播放
     */
    public void setSoundEnable(boolean mSoundEnable) {
        this.mSoundEnable = mSoundEnable;
        syncAllState();
    }

    /**
     * 设置是否开启音乐
     * @param mMusicEnable
     */
    public void setMusicEnable(boolean mMusicEnable) {
        this.mMusicEnable = mMusicEnable;
        syncAllState();

        if(mMusicEnable){
            //背景音乐开启
        }else{
            //背景音乐关闭
        }

    }

    /**
     * 设置是否关闭所有音乐相关声音
     * @param mAllEnable
     */
    public void setAllEnable(boolean mAllEnable) {
        this.mAllEnable = mAllEnable;
        syncAllState();

        if(!EmptyUtil.emptyOfString(bgMusic)){

            if(mAllEnable){
                //背景音乐开启
                playMusic(bgMusic,bgLooper);

//                for(Entry<String, Integer> mItem : mSounds.entrySet()){
//                    playSound(mItem.getKey(),false);
//                }

            }else{
                //背景音乐关闭
                pauseMusic();
                pauseSound();
            }
        }
    }

    /**
     * 播放音效
     *
     * @param fileName
     * @param isLoop
     */
    public void playSound(String fileName, boolean isLoop) {
        try {

            if(!mAllEnable || !mSoundEnable){
                return;
            }

            if(mSounds.containsKey(fileName) && mSoundPool!=null){

                int soundId = mSounds.get(fileName);
                mSoundPool.play(soundId, leftVolume, rightVolume, priority, 0, rate);
                //LogUtils.logEMsg("播放已经加载过的音效");

            }else {

                if(maxSoundPoolId>=MAX_SOUND_POOL_ID){
                    createSoundPool();
                }

                soundIsLoop = isLoop ? -1 : 0;
                AssetFileDescriptor asdFile = mContext.getResources().getAssets().openFd(fileName);
                int soundID = mSoundPool.load(asdFile, priority);
                maxSoundPoolId = soundID > maxSoundPoolId ? soundID : maxSoundPoolId;
                mSounds.put(fileName, soundID);
                logMsg("播放一个新的音效 : "+soundID);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logMsg(ex.toString());
        }
    }

    /**
     * 重新创建一个SoundPool
     */
    private void createSoundPool(){

        releaseSoundPool();

        //音效
        mSoundPool = new SoundPool(MAX_STREAMS, STREAM_TYPE, QUALITY);
        mSoundPool.setOnLoadCompleteListener(this);
        mSounds = new HashMap<String, Integer>();
    }

    /**
     * 释放SoundPool
     */
    public void releaseSoundPool(){
        if(mSoundPool!=null){
            mSoundPool.release();
            mSoundPool = null;
        }

        if(!EmptyUtil.emptyOfMap(mSounds)){
            mSounds.clear();
            mSounds = null;
        }

        maxSoundPoolId = 0;
    }

    /**
     * 暂停音效
     * @param fileName
     */
    public void pauseSound(String fileName) {

        int soundID = 0;
        if(!EmptyUtil.emptyOfString(fileName) && mSounds.containsKey(fileName)){
            soundID = mSounds.get(fileName);
            //LogUtils.logEMsg("SoundID" + soundID);
            if (soundID > 0 && mSoundPool != null) {
                mSoundPool.pause(soundID);
            }
        }
    }

    /**
     * 暂停所有音效
     */
    public void pauseSound() {

        if(mSounds!=null && mSounds.size()>0) {
            for (Entry<String, Integer> item : mSounds.entrySet()) {
                if (item.getValue() > 0 && mSoundPool != null) {
                    logMsg("停止音效:" + item.getValue());
                    mSoundPool.pause(item.getValue());
                }
            }
        }
    }

    /**
     * 停止音效
     * @param fileName
     */
    public void stopSound(String fileName) {

        if(!EmptyUtil.emptyOfString(fileName) && mSounds.containsKey(fileName)){

            int soundID = mSounds.get(fileName);

            if (soundID > 0 && mSoundPool != null) {
                mSoundPool.stop(soundID);
                mSoundPool.unload(soundID);
                mSounds.remove(fileName);
            }
        }
    }

    /**
     * 停止所有音效
     */
    public void stopSound() {
        logMsg("音效集合大小:" + mSounds.size());

        for (Entry<String, Integer> item : mSounds.entrySet()) {
            if (item.getValue() > 0 && mSoundPool != null) {
                boolean unLoad = mSoundPool.unload(item.getValue());
                mSoundPool.stop(item.getValue());
                logMsg("停止音效:" + item.getValue()+"---"+unLoad);
            }
        }
        mSounds.clear();
    }

    /**
     * 继续播放音效
     * @param fileName
     */
    public void resumeSound(String fileName){

        if(!EmptyUtil.emptyOfString(fileName) && mSounds.containsKey(fileName)){
            int soundId = mSounds.get(fileName);
            if(soundId>0 && mSoundPool!=null) {
                mSoundPool.resume(soundId);
            }
        }
    }

    /**
     * 打印消息
     * @param msg
     */
    public void logMsg(String msg) {
        if (DEBUG) {
            //LogUtils.logEMsg(TAG, msg);
        }
    }

    /**
     * 同步状态
     */
    private void syncAllState(){
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putBoolean("AllEnable",mAllEnable);
        mEditor.putBoolean("SoundEnable",mSoundEnable);
        mEditor.putBoolean("MusicEnable",mMusicEnable);
        mEditor.commit();
    }

    /**
     * 音乐是否开启
     * @return
     */
    public boolean isMusicEnabled(){
       return mPreferences.getBoolean("MusicEnable",false);
    }

    /**
     * 音效是否开启
     * @return
     */
    public boolean isSoundEnabled(){
        return mPreferences.getBoolean("SoundEnable",false);
    }

    /**
     * 音乐、音效是否开启
     * @return
     */
    public boolean isAllEnabled(){
        return mPreferences.getBoolean("AllEnable",mNormalState);
    }

}
