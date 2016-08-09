# RrtoyewxRecyclerView

# 更新日志
1. 2016.8.8 17:00 
    完成能够addHeaderView和addFooterView();
2. 2016.8.8 18:50
    - 完成removeHeaderView和removeFooterView();
    - 完成setEmptyView();
    - 修复在设置完LayoutManager后改变LayoutManager，headerView和footerView不占一行的bug;
3. 2016.8.9 18:00
    - 完成加载更多功能

----
# 目前支持的功能
1. 能够addHeaderView和addFooterView
```
    recyclerView.addHeader(View headerView);
    recyclerView.addFooter(View footerView);

```
2. 能够setEmpty()
```
    recyclerView.setEmpty(View emptyView);

```
3. 能够显示加载更多
```
    //设置自定义的loadMoreView,可以不设置
    recyclerView.setLoadMoreView(View loadMoreView);
    
   // 开启能够加载更多，必填 默认是false
   recyclerView.setLoadMoreEnable(boolean loadMoreEnable);
   
   //加载更多完成
   completeLoadMore();
   
   //增加加载更多的监听，主要是在回调方法去请求数据等操作，
   addRefreshListener(RefreshListener listener);
```
  
