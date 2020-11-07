[#ftl]
[@b.head/]
[@b.toolbar title="供应商"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="supplierSearchForm" action="!search" target="supplierlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="supplier.code;代码"/]
      [@b.textfields names="supplier.name;名称"/]
      <input type="hidden" name="orderBy" value="supplier.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="supplierlist" href="!search?orderBy=supplier.code"/]
  </div>
</div>
[@b.foot/]
