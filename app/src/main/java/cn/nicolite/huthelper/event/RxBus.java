package cn.nicolite.huthelper.event;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * RxBus 事件总线 带背压处理
 * Created by nicolite on 17-10-13.
 */

public class RxBus {

    private final FlowableProcessor<Object> bus;
    private static volatile RxBus instance;

    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault(){
        if (instance == null){
            synchronized (RxBus.class){
                if (instance == null){
                    instance = Holder.BUS;
                }
            }
        }
        return instance;
    }

    public void post(Object object){
        bus.onNext(object);
    }

    public <T> Flowable<T> toFlowable(Class<T> clazz){
        return bus.ofType(clazz);
    }

    public Flowable<Object> toFlowable(){
        return bus;
    }

    public boolean hasSubscribers(){
        return bus.hasSubscribers();
    }

    private static class Holder{
        private static final RxBus BUS = new RxBus();
    }
}
