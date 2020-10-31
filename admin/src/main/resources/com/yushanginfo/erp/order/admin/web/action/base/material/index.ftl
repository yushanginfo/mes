[#ftl]
[@b.head/]
[@b.toolbar title="品号信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="materialSearchForm" action="!search" target="materiallist" title="ui.searchForm" theme="search"]
      [@b.textfields names="material.code;品号"/]
      [@b.textfields names="material.name;品名"/]
      [@b.select name="material.materialType.id" label="类别" items=materialTypes empty="..."/]
      <input type="hidden" name="orderBy" value="material.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="materiallist" href="!search?orderBy=material.code desc"/]
  </div>
</div>
[@b.foot/]
