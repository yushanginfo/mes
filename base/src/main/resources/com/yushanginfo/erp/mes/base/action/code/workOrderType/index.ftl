[#ftl]
[@b.head/]
[@b.toolbar title="工单单别"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="workOrderTypeSearchForm" action="!search" target="workOrderTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="workOrderType.code;代码"/]
      [@b.textfields names="workOrderType.name;名称"/]
      <input type="hidden" name="orderBy" value="workOrderType.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="workOrderTypelist" href="!search?orderBy=workOrderType.code"/]
  </div>
</div>
[@b.foot/]
