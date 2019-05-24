package asad.com.myapplication.rxdownload;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableCreate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: RxBus
 * @Autor: Asia
 * @CreateDate: 2019/4/17  19:28
 * @UpdateDate: 2019/4/17  19:28
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class RxBus {
     private Subject<Object> mBus;

     private static  RxBus Instance;

     private   RxBus (){
        mBus = PublishSubject.create().toSerialized();
     }

     static  RxBus getInstance() {
         if (Instance == null) {
             synchronized (RxBus.class) {
                 if (Instance == null) {
                     Instance = new RxBus();
                 }
             }
         }

         return Instance;
     }
     public static void send (Object obj) {
            if (getInstance().getBus().hasObservers()){
                Log.e("TAG",obj.toString()+">>>>>>>>>>>>>>>");
                getInstance().getBus().onNext(obj);
            }
     }

       static  Observable<Object> toObservable() {
         return getInstance().getBus();
     }

      static  Observable<DownloadProcessEvent> getDownloadEventObservable() {


         return  getInstance().toObservable().ofType(DownloadProcessEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
     }



     private  Subject<Object> getBus () {
         return mBus;
     }



}
