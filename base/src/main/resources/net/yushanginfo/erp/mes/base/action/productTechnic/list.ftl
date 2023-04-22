[#ftl]
[@b.head/]
[@b.grid items=productTechnics var="productTechnic"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="12%" property="scheme.product.code" title="品号"/]
    [@b.col width="16%" property="scheme.product.name" title="品名"/]
    [@b.col width="10%" property="scheme.name" title="工艺路线"/]
    [@b.col width="7%" property="indexno" title="加工顺序"/]
    [@b.col width="13%" property="technic.name" title="工艺名称"]
      ${productTechnic.technic.code} ${productTechnic.technic.name}
    [/@]
    [@b.col width="7%" property="internal" title="性质"]
      [#if productTechnic.internal]厂内[#else]委外[/#if]
    [/@]
    [@b.col width="13%" title="加工中心/供应商"]
       [#if productTechnic.machine??]${productTechnic.machine.code} ${productTechnic.machine.name}
       [#elseif productTechnic.supplier??] ${productTechnic.supplier.code} ${productTechnic.supplier.name}
       [/#if]
    [/@]
    [@b.col property="description" title="说明"/]
  [/@]
[/@]
[@b.foot/]
