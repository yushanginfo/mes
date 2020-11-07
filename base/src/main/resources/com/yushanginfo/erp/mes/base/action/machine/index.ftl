[#ftl]
[@b.head/]
[@b.toolbar title="加工中心"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="machineSearchForm" action="!search" target="machinelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="machine.code;代码"/]
      [@b.textfields names="machine.name;名称"/]
      <input type="hidden" name="orderBy" value="machine.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="machinelist" href="!search?orderBy=machine.code"/]
  </div>
</div>
[@b.foot/]
