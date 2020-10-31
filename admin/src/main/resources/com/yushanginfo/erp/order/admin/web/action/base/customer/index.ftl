[#ftl]
[@b.head/]
[@b.toolbar title="客户信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="customerSearchForm" action="!search" target="customerlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="customer.code;客户编号"/]
      [@b.textfields names="customer.quickCode;快捷码"/]
      [@b.textfields names="customer.name;客户全称"/]
      [@b.textfields names="customer.saler.name;业务员"/]
      <input type="hidden" name="orderBy" value="customer.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="customerlist" href="!search?orderBy=customer.code desc"/]
  </div>
</div>
[@b.foot/]
