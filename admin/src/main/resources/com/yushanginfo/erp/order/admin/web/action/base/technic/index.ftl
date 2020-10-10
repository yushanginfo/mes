[#ftl]
[@b.head/]
[@b.toolbar title="工序管理"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="technicSearchForm" action="!search" target="techniclist" title="ui.searchForm" theme="search"]
      [@b.textfields names="technic.code;代码"/]
      [@b.textfields names="technic.name;名称"/]
      <input type="hidden" name="orderBy" value="technic.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="techniclist" href="!search?orderBy=technic.code"/]
  </div>
</div>
[@b.foot/]
