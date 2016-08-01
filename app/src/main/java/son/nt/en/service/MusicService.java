package son.nt.en.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.List;

import son.nt.en.base.IAudioFile;
import son.nt.en.otto.OttoBus;
import son.nt.en.service.notification.INotification;
import son.nt.en.service.notification.NotificationImpl;
import son.nt.en.utils.Logger;

public class MusicService extends Service
{
    INotification              iNotification;
    Playback                   iPlayback;

    public static final String TAG                    = "MusicService";
    public static final String ACTION_TOGGLE_PLAYBACK = "com.example.android.musicplayer.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY            = "com.example.android.musicplayer.action.PLAY";
    public static final String ACTION_PAUSE           = "com.example.android.musicplayer.action.PAUSE";
    public static final String ACTION_STOP            = "com.example.android.musicplayer.action.STOP";
    public static final String ACTION_SKIP            = "com.example.android.musicplayer.action.SKIP";
    public static final String ACTION_REWIND          = "com.example.android.musicplayer.action.REWIND";
    public static final String ACTION_URL             = "com.example.android.musicplayer.action.URL";




    public static Intent getService(Context context)
    {
        return new Intent(context, MusicService.class);
    }

    public static void bindToMe(Context context, ServiceConnection musicServiceConnection)
    {
        context.bindService(MusicService.getService(context), musicServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public class LocalBinder extends Binder
    {

        public MusicService getService()
        {
            return MusicService.this;
        }

    }

    enum State
    {
        Fetching, Stopped, Playing, Paused, Preparing
    }

    State                     mState        = State.Stopped;

    LocalBinder               localBinder   = new LocalBinder();
    MediaPlayer               mediaPlayer;

    AudioManager              audioManager;

    boolean                   isStreaming;
    String                    songTitle;

    IAudioFile                currentItem;
    int                       currentPos    = 0;

    float                     currentVolume = 0.7f;

    List<IAudioFile>          listMusic     = new ArrayList<>();

    MusicPlayback.Callback    musicPlayBackCallback;

    public MusicService()
    {
        Logger.debug(TAG, ">>>" + "MusicService");

    }

    private void setupMediaPlayerIfNeeded()
    {
        Logger.debug(TAG, ">>>" + "setupMediaPlayerIfNeeded");
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnErrorListener(onErrorListener);

        }
        else
        {
            mediaPlayer.reset();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Logger.debug(TAG, ">>>" + "onCreate");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        iNotification = new NotificationImpl(this, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationImpl.ACTION_NEXT);
        filter.addAction(NotificationImpl.ACTION_PREV);
        filter.addAction(NotificationImpl.ACTION_PLAY);
        filter.addAction(NotificationImpl.ACTION_CLOSE);
        registerReceiver(receiver, filter);

    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();

    }


    public void processTogglePlayback()
    {
        if (mState == State.Paused || mState == State.Stopped)
        {
            processPlayRequest();
        }
        else
        {
            processPauseRequest();
        }

    }

    public void processPlayRequest()
    {
        Logger.debug(TAG, ">>>" + "processPlayRequest");
        if (mState == State.Stopped)
        {
            if (!listMusic.isEmpty() && currentItem != null)
            {
                currentItem = listMusic.get(currentPos);
                playNextSong(currentItem);
            }
        }
        else if (mState == State.Paused)
        {
            mState = State.Playing;
//            setupAsForeGroundCustom(currentItem);
            play();
        }

    }

    public void playAtPos(int pos)
    {
        currentPos = pos;
        if (!listMusic.isEmpty())
        {
            currentItem = listMusic.get(currentPos);
            playNextSong(currentItem);
            //            play();
        }

    }

    public void processPauseRequest()
    {
        Logger.debug(TAG, ">>>" + "processPauseRequest");
        if (mState == State.Playing)
        {
            mState = State.Paused;
            mediaPlayer.pause();
            relaxResource(false);
        }

    }

    public void processAddRequest(IAudioFile audioFile)
    {
        Logger.debug(TAG, ">>>" + "processAddRequest");
        listMusic.clear();
        currentPos = 0;
        this.currentItem = audioFile;

        playNextSong(audioFile);

    }

    public void playNextSong(IAudioFile currentItem)
    {
        String url = currentItem.getLinkMp3();
        Logger.debug(TAG, ">>>" + "playNextSong:" + url);
        if (musicPlayBackCallback != null)
        {
            musicPlayBackCallback.onPreparing(currentItem);
        }
        mState = State.Stopped;

        relaxResource(false);
        try
        {
            setupMediaPlayerIfNeeded();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            isStreaming = url.startsWith("http:") || url.startsWith("https:");

            songTitle = url;

            mState = State.Preparing;
//            setupAsForeGroundCustom(currentItem);

            /**
             * @see onPreparedListener
             */

            mediaPlayer.prepareAsync();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setDataToService(List<? extends IAudioFile> list)
    {
        Logger.debug(TAG, ">>>" + "setDataToService:" + list.size());
        if (list == null || list.size() == 0)
        {
            return;
        }

        this.listMusic.clear();
        this.listMusic.addAll(list);
        //        if (type == 1)
        //        {
        //
        //            this.listMusic.clear();
        //            this.listMusic.addAll(list);
        //        } else
        //        {
        //            this.listEsl.clear();
        //            this.listEsl.addAll(list);
        //        }
        currentPos = 0;
        currentItem = listMusic.get(currentPos);
        //        playNextSong(currentItem);

    }

    void relaxResource(boolean isReleaseMediaPlayer)
    {
        stopForeground(true);
        if (isReleaseMediaPlayer && mediaPlayer != null)
        {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    public void play()
    {
        if (mediaPlayer.isPlaying())
        {
            iNotification.doPause();

            mediaPlayer.pause();
            OttoBus.post(new GoPlayer(GoPlayer.DO_PAUSE, currentItem.getTitle(), currentItem.getDescription(), currentItem.getImage()));
            return;
        }

        if (!mediaPlayer.isPlaying())
        {
            iNotification.doPlay();
            OttoBus.post(new GoPlayer(GoPlayer.DO_PLAY, currentItem.getTitle(), currentItem.getDescription(), currentItem.getImage()));
            mediaPlayer.start();
        }

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Logger.debug(TAG, ">>>" + "onBind");
        return localBinder;
    }

    MediaPlayer.OnPreparedListener   onPreparedListener   = new MediaPlayer.OnPreparedListener()
                                                          {
                                                              @Override
                                                              public void onPrepared(MediaPlayer mediaPlayer)
                                                              {
                                                                  Logger.debug(TAG, ">>>" + "onPrepared");
                                                                  if (musicPlayBackCallback != null)
                                                                  {
                                                                      musicPlayBackCallback.onPlaying(currentItem);
                                                                  }
                                                                  mState = State.Playing;

                                                                  iNotification.setData(currentItem);
                                                                  //                                                                  updateNotification(currentItem);
//                                                                  setupAsForeGroundCustom(currentItem);
                                                                  play();

                                                              }
                                                          };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener()
                                                          {
                                                              @Override
                                                              public void onCompletion(MediaPlayer mediaPlayer)
                                                              {
                                                                  if (listMusic.isEmpty())
                                                                  {
                                                                      return;
                                                                  }
                                                                  currentPos++;
                                                                  if (currentPos == listMusic.size())
                                                                  {
                                                                      currentPos = 0;
                                                                  }
                                                                  currentItem = listMusic.get(currentPos);
                                                                  playNextSong(currentItem);

                                                              }
                                                          };
    MediaPlayer.OnErrorListener      onErrorListener      = new MediaPlayer.OnErrorListener()
                                                          {
                                                              @Override
                                                              public boolean onError(MediaPlayer mediaPlayer, int i,
                                                                              int i1)
                                                              {
                                                                  //                                                                  if (listMusic.isEmpty())
                                                                  //                                                                  {
                                                                  //                                                                      return false;
                                                                  //                                                                  }
                                                                  //                                                                  currentPos++;
                                                                  //                                                                  if (currentPos == listMusic.size())
                                                                  //                                                                  {
                                                                  //                                                                      currentPos = 0;
                                                                  //                                                                  }
                                                                  //                                                                  currentItem = listMusic.get(currentPos);
                                                                  //                                                                  playNextSong(currentItem);
                                                                  return true;
                                                              }
                                                          };

    public void setMusicPlayBackCallback(MusicPlayback.Callback cb)
    {
        this.musicPlayBackCallback = cb;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(NotificationImpl.ACTION_PLAY))
            {
                play();
            }
            else if (action.equals(NotificationImpl.ACTION_NEXT))
            {

                playAtPos(currentPos ++);
            }
            else if (action.equals(NotificationImpl.ACTION_PREV))
            {
                playAtPos(currentPos --);
            }
            else if (action.equals(NotificationImpl.ACTION_CLOSE))
            {
                relaxResource(true);
//                if (mediaPlayer != null && mediaPlayer.isPlaying())
//                {
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//               stopForeground(true);
            }
        }
    };


}
