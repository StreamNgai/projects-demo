package dev.ngai.fantastic.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import dev.ngai.fantastic.Logc;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des: 任务调度；子线程耗时操作统一管理；提供取消方法；
 */
public class TaskScheduler {

    private static String TAG = "TaskScheduler";
    static private HashMap<String, BaseTask> mTasks = new HashMap<>();

    synchronized static public BaseTask execute(BaseTask task) {
        if (task != null) {
            String unique = task.uniqueTag();
            if (mTasks.containsKey(unique)) {
//                return mTasks.get(unique);
//                throw new RuntimeException("already contains " + unique + " task !");
                cancel(unique);
            }
            mTasks.put(unique, task);
            AsyncTask.THREAD_POOL_EXECUTOR.execute(task);
        }
        return task;
    }

    static public void cancel(String unique) {
        if (!TextUtils.isEmpty(unique)) {
            BaseTask task = mTasks.get(unique);
            if (task != null) {
                task.onInterrupt();
            }
        }
    }


    static public abstract class BaseTask implements Runnable {

        private AtomicReference<Thread> mTaskThread = new AtomicReference<>();
        Handler mHandler = new InternalHandler();
        CallBack mCallBack;

        public interface CallBack<T> {
            void onResult(T obj);
        }

        BaseTask() {
        }

        BaseTask(CallBack callBack) {
            this.mCallBack = callBack;
        }

        @Override
        public void run() {
            mTaskThread.compareAndSet(null, Thread.currentThread());
            onRun();
        }

        abstract void onRun();

        void onFinish(final Object obj) {
            if (mCallBack != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mCallBack.onResult(obj);
                        }catch (Exception e){
                            Logc.d(TAG,"callback obj must unified by onFinish result ! <<-----------------");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        public abstract String uniqueTag();

        private void onInterrupt() {
            Thread t = mTaskThread.get();
            if (t != null) {
                t.interrupt();
                Logc.d(TAG, "Task[" + uniqueTag() + "] cancel: " + t.getName());
            }
            onCancel();
        }

        abstract void onCancel();

        class InternalHandler extends Handler {
            InternalHandler() {
                super(Looper.getMainLooper());
            }
        }
    }
}
