[#ftl]
[@b.head/]
[@b.toolbar title="厂区管理"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="factorySearchForm" action="!search" target="factorylist" title="ui.searchForm" theme="search"]
      [@b.textfields names="factory.code;编码"/]
      [@b.textfields names="factory.name;名称"/]
      <input type="hidden" name="orderBy" value="factory.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="factorylist" href="!search?orderBy=factory.code"/]
  </div>
</div>
[@b.foot/]
