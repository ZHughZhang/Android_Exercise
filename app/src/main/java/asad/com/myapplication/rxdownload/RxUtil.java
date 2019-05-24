package asad.com.myapplication.rxdownload;

import android.annotation.SuppressLint;
import android.util.AndroidException;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: RxUtil
 * @Autor: Asia
 * @CreateDate: 2019/4/15  16:23
 * @UpdateDate: 2019/4/15  16:23
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class RxUtil {

    private  static  final String TAG = RxUtil.class.getSimpleName();

    private static  volatile  RxUtil Instance;

    private Set<Object> subscribers;

    private RxUtil (){
        subscribers = new CopyOnWriteArraySet<>();
    }

    public void chainProcess (Function func) {

        Observable.just("").subscribeOn(Schedulers.io())
                .map(func)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        for (Object obj : subscribers){
                            callMethodByAnnotiation(obj,o);
                        }
                    }
                });
    }

    private void callMethodByAnnotiation(Object target, Object data)  {
        Method[] methodArray = target.getClass().getDeclaredMethods();
        Log.d(TAG, "callMethodByAnnotiation: methodArray"+methodArray.length);
        for (int i = 0; i < methodArray.length;i++) {
            Log.d(TAG, "callMethodByAnnotiation: for"+i);
            try {
                Log.e(TAG,methodArray[i].getAnnotation(RxBusRegister.class)+">>>");
                if (methodArray[i].isAnnotationPresent(RxBusRegister.class)) {
                    Class paramType  = methodArray[i].getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())){
                        methodArray[i].invoke(target,new Object[]{data});
                    }
                }else {
                    Log.e(TAG,methodArray[i].isAnnotationPresent(RxBusRegister.class)+">>>>>>>>>>>");
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "callMethodByAnnotiation: "+e.getMessage() );
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Log.e(TAG, "callMethodByAnnotiation: "+e.getMessage() );
                e.printStackTrace();
            }
        }

    }

    public  synchronized  void register(Object obj){
        subscribers.add(obj);
    }

    public synchronized void unRegister(Object obj) {
        subscribers.remove(obj);
    }

    public  void  send(final Object data) {

        Observable observable =Observable.create(new ObservableOnSubscribe () {

                              @Override
                              public void subscribe(ObservableEmitter emitter) throws Exception {
                                        emitter.onNext(data);
                                        emitter.onComplete();
                              }
                          });
               observable .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        
                    }

                    @Override
                    public void onNext(Object o) {
                        synchronized (RxUtil.class){
                            Log.e(TAG,subscribers.size()+">>>>>>>>>>>>>>>>>");
                            for (Object obj: subscribers) {
                                callMethodByAnnotiation(obj,data);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public  static synchronized  RxUtil  getInstance( ) {
            if  (Instance == null) {
                synchronized (RxUtil.class){
                    if (Instance == null){
                        Instance = new RxUtil();
                    }
                }
            }

            return Instance;
    }



}
