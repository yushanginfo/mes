[#ftl]
[@b.head/]
[@b.toolbar title="订单类型"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="orderTypeSearchForm" action="!search" target="orderTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="orderType.code;代码"/]
      [@b.textfields names="orderType.name;名称"/]
      <input type="hidden" name="orderBy" value="orderType.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="orderTypelist" href="!search?orderBy=orderType.code"/]
  </div>
</div>
[@b.foot/]
