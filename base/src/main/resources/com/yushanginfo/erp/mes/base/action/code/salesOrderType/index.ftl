[#ftl]
[@b.head/]
[@b.toolbar title="订单类型"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="salesOrderTypeSearchForm" action="!search" target="salesOrderTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="salesOrderType.code;代码"/]
      [@b.textfields names="salesOrderType.name;名称"/]
      <input type="hidden" name="orderBy" value="salesOrderType.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="salesOrderTypelist" href="!search?orderBy=salesOrderType.code"/]
  </div>
</div>
[@b.foot/]
