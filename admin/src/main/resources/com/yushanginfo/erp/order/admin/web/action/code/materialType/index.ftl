[#ftl]
[@b.head/]
[@b.toolbar title="品号类型"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="materialTypeSearchForm" action="!search" target="materialTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="materialType.code;代码"/]
      [@b.textfields names="materialType.name;名称"/]
      <input type="hidden" name="orderBy" value="materialType.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="materialTypelist" href="!search?orderBy=materialType.code"/]
  </div>
</div>
[@b.foot/]
