[#ftl]
[@b.head/]
[@b.toolbar title="产品材料清单管理"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="itemSearchForm" action="!search" target="itemlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="item.product.code;代码"/]
      [@b.textfields names="item.product.name;名称"/]
      <input type="hidden" name="orderBy" value="item.product.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="itemlist" href="!search?orderBy=item.product.code"/]
  </div>
</div>
[@b.foot/]
