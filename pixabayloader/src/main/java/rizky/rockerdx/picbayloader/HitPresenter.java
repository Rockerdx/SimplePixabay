package rizky.rockerdx.picbayloader;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hendry on 27/01/19.
 */
public class HitPresenter {

    private CompositeDisposable composite = new CompositeDisposable();

    private View view;

    private int totalPage = 0;

    public interface View {
        void onSuccessGetData(List<Hit> articleList, boolean refresh);

        void onErrorGetData(Throwable throwable);

        void onEmptyNews();

    }

    public HitPresenter(HitPresenter.View view) {
        this.view = view;
    }

    public void getEverything(final Context context, final String keyword, String key,int page, final boolean refresh) {
        NewsDataSource.getService().getEverything(keyword, key,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        composite.add(d);
                    }

                    @Override
                    public void onNext(Data data) {
                        Log.d("tes","article success");
                        if(data!=null) {
                                totalPage = data.getTotalHits();
                                view.onSuccessGetData(data.getHits(), refresh);
                            } else
                                view.onEmptyNews();
                        }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("tes","article error");

                        view.onErrorGetData(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public int getTotalPage() {
        return (int) Math.floor(totalPage / 20);
    }

    public void unsubscribe() {
        composite.dispose();
    }
}
