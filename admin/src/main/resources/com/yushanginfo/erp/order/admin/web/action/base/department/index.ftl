[#ftl]
[@b.head/]
[@b.toolbar title="部门管理"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="departmentSearchForm" action="!search" target="departmentlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="department.code;代码"/]
      [@b.textfields names="department.name;名称"/]
      [@b.textfields names="department.indexno;序号"/]
      [@b.select name="department.parent.id" label="上级部门" items=departments empty="..." /]
      <input type="hidden" name="orderBy" value="department.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="departmentlist" href="!search?orderBy=department.code"/]
  </div>
</div>
[@b.foot/]
