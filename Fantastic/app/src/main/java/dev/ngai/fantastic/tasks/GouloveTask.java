package dev.ngai.fantastic.tasks;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des:
 */
public class GouloveTask extends TaskScheduler.BaseTask {

    public GouloveTask() {
    }

    public GouloveTask(CallBack callBack) {
        super(callBack);
    }

    @Override
    void onRun() {

        onFinish("123");
    }


    @Override
   public String uniqueTag() {
        return "GouloveTask";
    }

    @Override
    void onCancel() {

    }

}
