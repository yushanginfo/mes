[#ftl]
[@b.head/]
[#include "../technicScheme/nav.ftl"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="productTechnicSearchForm" action="!search" target="productTechniclist" title="ui.searchForm" theme="search"]
      [@b.textfields names="productTechnic.scheme.product.code;品号"/]
      [@b.textfields names="productTechnic.scheme.product.name;品名"/]
      [@b.select name="productTechnic.internal" label="性质" items={"1":'厂内',"0":'委外'} empty="..."/]
      [@b.textfields names="productTechnic.technic.name;工艺名称"/]
      [@b.textfields names="productTechnic.machine.name;加工中心,productTechnic.supplier.name;供应商"/]
      <input type="hidden" name="orderBy" value="productTechnic.scheme.product.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="productTechniclist" href="!search?orderBy=productTechnic.scheme.product.code desc"/]
  </div>
</div>
[@b.foot/]
