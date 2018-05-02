import okhttp3.OkHttpClient;
import org.junit.Test;
import vip.ipav.okhttp.OkHttpClientTools;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class HttpThread {

    @Test
    public void multiThreadHttp() throws Exception {
        ThreadLocal<OkHttpClient> myOkHttpClient = new ThreadLocal<>();
        myOkHttpClient.set(new OkHttpClient());
        String temp =
                new OkHttpClientTools(myOkHttpClient.get())
                        .get()
                        .url("http://api.t.sina.com.cn/short_url/shorten.json")
                        .addParam("source","3271760578")
                        .addParam("url_long","http://www.douban.com/note/249723561")
                        .execute().body().string();
        System.out.println(temp);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    testSyncGet();
                    testSyncGet();
                    testSyncGet2();
                    testSyncGet2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();
    }

    public void testSyncGet() throws IOException {
        String temp =
                OkHttpClientTools.getInstance()
                        .get()
                        .url("http://api.t.sina.com.cn/short_url/shorten.json")
                        .addParam("source","3271760578")
                        .addParam("url_long","http://www.douban.com/note/249723561")
                        .execute().body().string();
        System.out.println(temp);
    }

    public void testSyncGet2() throws IOException {
        String temp =
                new OkHttpClientTools(new OkHttpClient())
                        .get()
                        .url("http://api.t.sina.com.cn/short_url/shorten.json")
                        .addParam("source","3271760578")
                        .addParam("url_long","http://www.douban.com/note/249723561")
                        .execute().body().string();
        System.out.println(temp);
    }

    @Test
    public void testThreadLocal() throws InterruptedException {
        /*创建线程局部变量，使之线程安全*/
        ThreadLocal<String> myLocalString = new ThreadLocal<>();
        myLocalString.set("www.ipav.vip");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.printf("%s\t%d\n",myLocalString.get(),0);
                countDownLatch.countDown();
            }
        }).start();
        System.out.printf("%s\t%d\n",myLocalString.get(),1);
        countDownLatch.await();
    }
}