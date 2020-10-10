[#ftl]
[@b.head/]
[@b.toolbar title="用户信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="userSearchForm" action="!search" target="userlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="user.code;工号"/]
      [@b.textfields names="user.name;姓名"/]
      [@b.select name="user.department.id" label="所在部门" items=departments empty="..." style="width:100px"/]
      <input type="hidden" name="orderBy" value="user.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="userlist" href="!search?orderBy=user.code desc"/]
  </div>
</div>
[@b.foot/]
