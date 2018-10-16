package com.jazzi.onlinestudy;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*别忘记导入正确的Toolbar，安卓的Toolbar有多个，会有歧义
* 将实例调入setSupportActionBar应用成功即可*/
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    /*滑动图片所需的***************************/
    public ViewPager viewPager;
    public TextView title;
    public LinearLayout point ;
    public int imageId[];
    public String suggestion[];
    public List<ImageView> mImageViewList;
    private int prepos=0;
    /*滑动图片所需的***************************/


    /**合作高校所需***********************/
    private List<School> schools;
    private RecyclerView schoolLayout;
    /**合作高校所需***********************/




    private DrawerLayout mDrawerLayout;
    private Fruit[] fruits={new Fruit("人工智能",R.drawable.pingguo),
        new Fruit("玩转流行音乐",R.drawable.boluo),
        new Fruit("高等数学",R.drawable.caomei),
        new Fruit("设计的力量",R.drawable.juzi)};

    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter fruitAdapter;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*滑动图片所需的***************************/
        initView();
        initData();
        initMyAdapter();
        /*滑动图片所需的***************************/




        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*设置滑动列表布局*/
        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);


        /*该功能已经取消，登录放在了浮选按钮上*/
//        /*actionBar默认是在左上角的一个返回箭头，我们改了它的样式与作用
//        * setDisplayHomeAsUpEnabled是让导航按钮显现出来
//        * setHomeAsUpIndicator设置导航按钮的图标*/
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        /*获得滑动窗口实例
        * 开启选项监听器
        * 设置点击选项触发事件，关闭滑动窗口*/
        NavigationView navView =(NavigationView) findViewById(R.id.nav_view);
        /*选中清除缓存项*/
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /*触发一个动画效果的提示框
            * 用make创建Snackbar对象，
            * 参数1：当前布局任意的View，Snackbar会利用参数1找到最外层布局，用于展示Snackbar
            * 参数2：显示的内容
            * 参数3：显示的时长*/
            @Override
            public void onClick(View view) {
                /*登录放在此按钮上，原来的功能弃用*/
//                Snackbar.make(view,"删除数据",Snackbar.LENGTH_SHORT)
//                        .setAction("撤销", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(MainActivity.this,"数据已经还原",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();

                /*开启滑动列表*/
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /*初始化fruitList
        * 创建一个2列的网格布局
        * 放入可滑动布局recyclerView
        * 创建水果适配器
        * 适配器+控件=最终结果*/
        initFruits();
        RecyclerView fruitRecyclerView=(RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        fruitRecyclerView.setLayoutManager(layoutManager);
        fruitAdapter =new FruitAdapter(fruitList);
        fruitRecyclerView.setAdapter(fruitAdapter);

        /*setColorSchemeResources来设置下来刷新进度条的颜色
        * 设置下拉刷新器，当触发下拉的时候，就会调用这个监听器的onRefresh()方法*/
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });





        /**合作高校所需***********************/
        initSchools();
        schoolLayout=(RecyclerView) findViewById(R.id.school_view);
        LinearLayoutManager l=new LinearLayoutManager(this);
        //设置为x轴方向滑动
        l.setOrientation(LinearLayoutManager.HORIZONTAL);
        schoolLayout.setLayoutManager(l);//指定内部局为线性布局
        //创建适配器
        SchoolAdapter SchoolAdapter =new SchoolAdapter(schools);
        //适配器+控件==最终结果
        /*mmp,mmp,mmp我设置适配器设置到推荐课程的布局里去了，caocoacaocoacao
        * 论起一个精确名字的重要性*/
        schoolLayout.setAdapter(SchoolAdapter);


        /**合作高校所需***********************/

    }

    /**合作高校所需***********************/
    private void initSchools(){
        schools=new ArrayList<School>();
        schools.add(new School("清华大学",R.mipmap.qinghua));
        schools.add(new School("北京大学",R.mipmap.beida));
        schools.add(new School("厦门大学",R.mipmap.xiada));
        schools.add(new School("武汉大学",R.mipmap.wuda));
        schools.add(new School("人民大学",R.mipmap.renda));
        schools.add(new School("浙江大学",R.mipmap.zheda));
    }



    /**合作高校所需***********************/






    /*先是开启了一个线程，然后将线程沉睡两秒
    * 沉睡结束后，runOnUiThread方法将线程切换回主线程
    * 然后调用 initFruits();重新生成图片数据
    * 接着再调用notifyDataSetChanged将滑动菜单适配器刷新数据
    * 最后隐藏刷新进度条，表示刷新时间结束*/
    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        fruitAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        }).start();
    }
    /*将fruitList容器清空
    * 并且随机取fruits中50个可重复的Fruit对象加入fruitList中*/
    private void initFruits(){
        fruitList.clear();
        for(int i=0;i<12;++i){
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }



    /*getMenuInflater()方法先得到MenuInflater对象，再用这个对象inflate()方法给活动添加菜单
    inflate()方法接收两个参数
    参数1：资源文件
    参数2：指定我们菜单项将添加到哪个Menu对象中
    menu可能会在运行时自动接收活动中的菜单对象，然后对菜单对象进行显示操作。
    默认就是toolbar的菜单对象。
    返回true，将显示菜单
    返回flase，不会显示菜单
    自动加载菜单方法，自动运行。*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    /*这个方法中处理菜单中各个按钮的点击事件
    * 左上角返回按钮的id默认是android.R.id.home
    * openDrawer即把这个滑动列表展示出来*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*该功能已被弃用，登录放在浮悬按钮上了*/
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                break;
//            case R.id.backup:
//                Toast.makeText(this,"你点击了返回",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.delete:
//                Toast.makeText(this,"你点击了删除",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.settings:
//                Toast.makeText(this,"你点击了设置",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item_search:
//                break;
            default:
                break;
        }
        return true;
    }


    /*滑动图片所需的***************************/
    public void initView(){
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        /*设置滚动图片点击事件监听器，监听器在main活动中实现*/
        viewPager.setOnPageChangeListener(this);

        title=(TextView)findViewById(R.id.tv_desc);
        point=(LinearLayout)findViewById(R.id.ll_point_container);
    }

    /*初始化数据*/
    public void initData() {
        imageId = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d};
        suggestion = new String[]{
                "关于什么是“智能”，就问题多多了",
                "人工智能在计算机领域内，得到了愈加广泛的重视",
                "二十世纪七十年代以来被称为世界三大尖端技术之一",
                "“机器学习”的数学基础是“统计学”、“信息论”和“控制论”"
        };
        mImageViewList = new ArrayList<ImageView>();

        for (int i = 0; i < imageId.length; ++i) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageId[i]);
            mImageViewList.add(imageView);

            /*创建了一个小窗体，
             * 该窗体的背景是selector_bg_point图片
             * 设置一个LinearLayout的内布局，设定其中每个子窗体的大小是宽100，高50
             * 并且每个子窗体一开始为不可用状态
             * 将内部局加入到LinearLayout对象中*/
            View pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);

            pointView.setEnabled(false);
            point.addView(pointView, layoutParams);

        }
    }

    /*初始化适配器*/
    public void initMyAdapter(){
        point.getChildAt(0).setEnabled(true);
        viewPager.setAdapter(new MyAdapter());
    }

    /*建立一个适配器*/
    public class MyAdapter extends PagerAdapter {

        /*获取需要展示在ViewPager的View总数*/
        @Override
        public int getCount() {
            return mImageViewList.size();
        }



        /*返回的是view复用规则
         * 判断当前轮播框显示的view是否与方法instantiateltem返回来的object相同
         * 诺相同则会在viewPage里显示出对应的view了
         *
         * 即判断当拖动到一定距离后，开始触发切换事件*/
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        /*向ViewGroup容器里添加view
         * 返回需要展示的view
         *
         * position表示拖动到的下一个位置i
         * 为了防止出现错误，%size防止溢出*/

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition=position%mImageViewList.size();
            ImageView imageView=mImageViewList.get(newPosition);
            container.addView(imageView);
            return imageView;
        }

        /*移除ViewGroup中已经展示过的view
         * 销毁已经展示的view*/
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }



    }


    /*以下四个方法实现了监听器*/


    /*当条目被选中时调用
     * 注：切换图片后自动选中切换后的图片*/
    @Override
    public void onPageSelected(int position) {
        int newPosition=position%mImageViewList.size();
        title.setText(suggestion[newPosition]);

        point.getChildAt(prepos).setEnabled(false);
        point.getChildAt(newPosition).setEnabled(true);
        prepos=newPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /*滑动图片所需的***************************/
}
