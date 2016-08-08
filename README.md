# RrtoyewxRecyclerView

# 更新日志
1. 2016.8.8 17:00 
    完成能够addHeaderView和addFooterView();
2. 2016.8.8 18:50
    - 完成removeHeaderView和removeFooterView();
    - 完成setEmptyView();
    - 修复在设置完LayoutManager后改变LayoutManager，headerView和footerView不占一行的bug;


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
  
