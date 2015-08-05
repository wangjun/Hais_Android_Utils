package pw.hais.utils_demo.activity;

import android.os.Bundle;

import pw.hais.utils_demo.R;
import pw.hais.utils_demo.app.BaseActivity;
import pw.hais.view.TextHtmlImageView;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextHtmlImageView text_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String html = "<img src='http://cdn.duitang.com/uploads/item/201210/24/20121024114700_5JveU.jpeg'/><br/>"+
                        "<img src='http://attach.bbs.miui.com/forum/201408/29/123450r617238484s76q78.jpg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img1.gamedog.cn/2014/05/24/119-1405241001530.jpg'/><br/>"+"<img src='http://attach.bbs.miui.com/forum/201408/29/123450r617238484s76q78.jpg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img1.gamedog.cn/2014/05/24/119-1405241001530.jpg'/><br/>"+"<img src='http://attach.bbs.miui.com/forum/201408/29/123450r617238484s76q78.jpg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img1.gamedog.cn/2014/05/24/119-1405241001530.jpg'/><br/>"+"<img src='http://attach.bbs.miui.com/forum/201408/29/123450r617238484s76q78.jpg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img2.duitang.com/uploads/item/201302/02/20130202212707_4isX8.jpeg'/><br/>"+
                        "<img src='http://img1.gamedog.cn/2014/05/24/119-1405241001530.jpg'/><br/>"+
                        "<img src='http://img5q.duitang.com/uploads/item/201210/24/20121024114719_2J8Gu.jpeg'/><br/>";
        text_hello.setHtml(html);

    }

}
