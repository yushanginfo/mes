[#ftl]
[@b.head/]
[@b.toolbar title="产品信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="productSearchForm" action="!search" target="productlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="product.code;编号"/]
      [@b.textfields names="product.name;名称"/]
      <input type="hidden" name="orderBy" value="product.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="productlist" href="!search?orderBy=product.code desc"/]
  </div>
</div>
[@b.foot/]
