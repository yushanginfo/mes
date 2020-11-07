[#ftl]
[@b.head/]
[@b.toolbar title="产品信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="productSearchForm" action="!search" target="productlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="product.code;编号"/]
      [@b.textfields names="product.name;名称"/]
      [@b.select name="product.materialType.id" label="类别" items=materialTypes empty="..."/]
      [@b.select name="bomNum" label="bom数量" items={"-1":"仅有一个","1":"一个以上","2":"多个","0":"无"} empty="..." /]
      [@b.select name="technicSchemeNum" label="工艺路线" items={"-1":"仅有一个","1":"一个以上","2":"多个","0":"无"} empty="..." /]
      <input type="hidden" name="orderBy" value="product.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="productlist" href="!search?orderBy=product.code desc"/]
  </div>
</div>
[@b.foot/]
