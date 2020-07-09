package jp.kosuke.kinoshita.rxjava2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // String型で
        Observable.create<String> {

            //ここは別スレッドで動作する
            Log.v("nullpo", Thread.currentThread().name)

            try {
                //何回もデータを送信できる
                //ここで逐次に発生するデータを送る
                it.onNext("テスト")
                it.onNext("テスト")
                it.onNext("テスト")

                //成功したらonComplete
                it.onComplete()
            } catch (e: Exception) {
                //エラーならonErrorを実行する
                it.onError(Throwable("Error"))
            }
        }
            .subscribeOn(Schedulers.newThread())

            //これ以降はメインスレッドで実行する
            .observeOn(AndroidSchedulers.mainThread())

            //onNextが実行するたびに呼ばれる（他が1回に対して任意の回数呼べる）
            //ラムダ式のitにデータが入っているので、そのデータに対して処理を行う
            .doOnNext {
                //メインスレッドで実行されるのでUI部品をいじれる
                Log.v("nullpo", Thread.currentThread().name)
                Log.v("null", "onNext")
            }

            //onErrorが実行されると呼ばれる
            .doOnError {
                Log.v("nullpo", Thread.currentThread().name)
                Log.v("null", "onError")
            }

            //onCompleteが実行されると呼ばれる
            .doOnComplete {
                Log.v("nullpo", Thread.currentThread().name)
                Log.v("null", "onComplete")
            }

            //onErrorを実行するときにはsubscribeでError処理をする引数のあるsbscripeを呼ばないと落ちる
            //なのでとりあえず何もしないラムダ式を記述しておく
            .subscribe({}, {})
    }
}