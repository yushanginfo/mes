[#ftl]
[@b.head/]
[@b.toolbar title="计量单位"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="measurementUnitSearchForm" action="!search" target="measurementUnitlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="measurementUnit.code;代码"/]
      [@b.textfields names="measurementUnit.name;名称"/]
      <input type="hidden" name="orderBy" value="measurementUnit.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="measurementUnitlist" href="!search?orderBy=measurementUnit.code"/]
  </div>
</div>
[@b.foot/]
