package com.example.HLoggerForAndroid.HLogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by liubo on 14-9-25.
 */
public class LogFile
{

    //内存中日志文件最大值，3kb
    private static final int LOG_MAX_SIZE = 5  * 1024;
    //每隔120秒，上传一次。
    private static final int LOG_UPLOAD_TIME = 120;


    /*
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

            }
        }
    };

    private Thread thread = new Thread( new Runnable()
    {
        @Override
        public void run()
        {
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    });
      */


    public interface ILog
    {
        public String getHeaderInfo();
    }

    private static LogFile logFile;

    private static String TAG = "";

    FileWriter objFilerWriter = null;
    BufferedWriter objBufferedWriter = null;
    Handler writeHandler = null;
    Handler uploadHandler = null;


    public static LogFile getInstance(String tagInfo)
    {
        TAG = tagInfo;
        logFile = new LogFile();
        return logFile;
    }

    public LogFile()
    {
        writeHandler = new Handler();
        uploadHandler = new Handler();
    }

    // 存放日志文件的目录全路径
    private String m_strLogFolderPath = "";

    /**
     * 设置日志的路径
     **/
    private String getSDPath()
    {
         do
         {
             String state = Environment.getExternalStorageState();
             // 未安装 SD 卡
             if ( true != Environment.MEDIA_MOUNTED.equals( state ) )
             {
                 android.util.Log.d( TAG, "未安装SD卡!" );
                 break;
             }
             // SD 卡不可写
             if ( true == Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) )
             {
                 android.util.Log.d( TAG, "SD卡不可写!" );
                 break;
             }
             // 只有存在外部 SD 卡且可写入的情况下才允许保存日志文件到指定目录路径下
             // 没有指定日志文件存放位置的话，就写到默认位置，即 SD 卡根目录下的 custom_android_log 目录中
             if ( true == m_strLogFolderPath.trim().equals( "" ) )
             {
                 String strSaveLogPath = Environment.getExternalStorageDirectory() +
                         "/logs";
                 File fileSaveLogFolderPath = new File( strSaveLogPath );
                 // 保存日志文件的路径不存在的话，就创建它
                 if ( true != fileSaveLogFolderPath.exists() )
                 {
                     fileSaveLogFolderPath.mkdirs();
                 }
                 // 如果这里保存日志文件的路径还不存在的话，则要提醒用户了
                 if ( true != fileSaveLogFolderPath.exists() )
                 {
                     android.util.Log.d( TAG, "创建logs目录失败！" );
                     break;
                 }
                 // 指定日志文件保存的路径，文件名由内部按日期时间形式
                 m_strLogFolderPath = strSaveLogPath;
             }
         }while (false);

        return m_strLogFolderPath;
    }

    /**
     * 返回data路径
     **/
    private String getDataPath()
    {
//        Context context = ContextUtil.getInstance();
//        File file = context.getDir("logs",Context.MODE_PRIVATE);

        String str = getSDPath();

        return str;
    }

    /**
     * 获得文件的名称
     **/
    private String createName()
    {
        String strDateTimeFileName = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
        return strDateTimeFileName+".log";
    }

    /**
     * 创建文件
     **/
    private boolean createFile()
    {
        boolean isCreate = true;
        String logName = createName();
        File fileLogFilePath = new File( getDataPath(), logName);
        // 如果日志文件不存在，则创建它
        if ( true != fileLogFilePath.exists() )
        {
            try
            {
                fileLogFilePath.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                isCreate = false;
            }
        }
        // 如果执行到这步日志文件还不存在，就不写日志到文件了
        if ( true != fileLogFilePath.exists() )
        {
            android.util.Log.d( TAG, "创建日志文件失败！" );
            isCreate = false;
        }

        if (isCreate)
        {
            saveLogInfo(logName);
        }


        return isCreate;
    }

    /**
     * 保存日志
     **/
    public void writeLogFile(String headerMsg)
    {
        do // 非循环，只是为了减少分支缩进深度
        {
            File fileLogFilePath = new File( getDataPath(), getLogInfo());
            try
            {
                if (objFilerWriter == null)
                {
                    // 续写不覆盖
                    objFilerWriter = new FileWriter(fileLogFilePath,true );
                }

            }
            catch (IOException e1)
            {
                android.util.Log.d( TAG, "写入文件失败！" );
                e1.printStackTrace();
                break;
            }
            if (objBufferedWriter == null)
            {
                objBufferedWriter = new BufferedWriter( objFilerWriter );
            }

            try
            {
                objBufferedWriter.write( headerMsg );
                objBufferedWriter.flush();
            }
            catch (IOException e)
            {
                android.util.Log.d( TAG, "objBufferedWriter.write or objBufferedWriter.flush failed" );
                e.printStackTrace();
            }
        }while( false );
    }



    class WriteLogRunnable implements Runnable
    {
        private String msgStr;
        private ILog iLogImpl;

        public WriteLogRunnable(String msg,ILog iLog)
        {
            msgStr = msg;
            iLogImpl = iLog;
        }

        @Override
        public void run()
        {
            readyWrite(msgStr,iLogImpl);
        }
    }


    public void readyWrite(String msgStr,ILog iLogImpl)
    {
        String logName = getLogInfo();
        if (logName.equals(""))
        {
            boolean isCreate = createFile();
            if (isCreate)
            {
                String headerStr = iLogImpl.getHeaderInfo();
                writeLogFile(headerStr);
                writeLogFile(msgStr);
            }
        }
        else
        {
            writeLogFile(msgStr);
        }

        boolean isOver = isLogOverSize();
        if(isOver)
        {
            //先打zip包
            try
            {
                String filePath = getSDPath()+java.io.File.separator+getLogInfo();
                String zipPath =  getSDPath()+java.io.File.separator+getLogInfo().replace(".log",".zip");
                ZipUtil.zipFolder(filePath,zipPath);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            //创建新的txt文本
            boolean isCreate = createFile();
            if (isCreate)
            {
                String headerStr = iLogImpl.getHeaderInfo();
                writeLogFile(headerStr);
            }
        }
    }



    /**
     * 有回调函数的保存方法
     **/
    public void writeLogFile(final String strMsg,final ILog iLog )
    {
        WriteLogRunnable logRunnable = new WriteLogRunnable(strMsg,iLog);
        writeHandler.post(logRunnable);
    }

    /**
     * 判断文件大小
     **/
    private boolean isLogOverSize()
    {
        File file = new File(getDataPath(),getLogInfo());
        if (file.length() >= LOG_MAX_SIZE)
        {
            android.util.Log.d(TAG, "文件过大");

            if ( null != objBufferedWriter )
            {
                try
                {
                    objBufferedWriter.close();
                    objBufferedWriter = null;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if ( null != objFilerWriter )
            {
                try
                {
                    objFilerWriter.close();
                    objFilerWriter = null;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return true;
        }
        else
        {
            return false;
        }

    }


    private void saveLogInfo(String logName)
    {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences("config",Context.MODE_PRIVATE);
        Editor editor=sharedPreferences.edit();
        editor.putString("logName",logName);
        editor.commit();
    }

    private String getLogInfo()
    {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences("config",Context.MODE_PRIVATE);
        String logName = sharedPreferences.getString("logName","");
        return logName;
    }

    public void uploadInfo()
    {
        File sdFile = new File(getSDPath());
        File allFile[] = sdFile.listFiles();
        for (int i = 0;i<allFile.length;i++)
        {
            File subFile = allFile[i];
            String name = subFile.getName();
            Log.d(TAG,name);
            if (name.contains(".zip"))
            {
                UploadRunnable uploadRunnable = new UploadRunnable(name);
                uploadHandler.post(uploadRunnable);
            }
        }
    }


    class UploadRunnable implements Runnable
    {
        private String fileName;
        public UploadRunnable(String name)
        {
            fileName = name;
        }

        @Override
        public void run()
        {
            RequestParams params = new RequestParams();
            final File myFile = new File(getSDPath()+File.separator+fileName);
            try
            {
                params.put("file", myFile);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://192.168.20.130:8082/log/rest/file",params,new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes)
                {
                    Log.d(TAG,"success");
                    myFile.delete();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable)
                {
                    Log.d(TAG,"failure=="+throwable);

                }
            });
        }
    }


}
