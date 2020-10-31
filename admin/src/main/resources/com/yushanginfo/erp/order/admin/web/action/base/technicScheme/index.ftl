[#ftl]
[@b.head/]
[@b.toolbar title="工艺路线"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="technicSchemeSearchForm" action="!search" target="technicSchemelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="technicScheme.product.code;品号"/]
      [@b.textfields names="technicScheme.product.name;品名"/]
      [@b.textfields names="technicScheme.indexno;编号"/]
      [@b.textfields names="technicScheme.name;名称"/]
      <input type="hidden" name="orderBy" value="technicScheme.product.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="technicSchemelist" href="!search?orderBy=technicScheme.product.code desc"/]
  </div>
</div>
[@b.foot/]
