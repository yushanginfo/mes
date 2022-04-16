[#ftl]
[@b.head/]
[@b.grid items=technicSchemes var="technicScheme"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="20%" property="product.code" title="产品编号"/]
    [@b.col width="20%" property="product.name" title="产品名称"/]
    [@b.col width="10%" property="indexno" title="路线编号"/]
    [@b.col width="10%" property="name" title="路线名称"][@b.a href="!info?id=${technicScheme.id}"]${technicScheme.name}[/@][/@]
    [@b.col width="40%" title="工艺列表"]
      [#list technicScheme.technics! as technic]
        ${(technic.technic.name)!}[#if technic_has_next],[/#if]
      [/#list]
    [/@]
  [/@]
[/@]
[@b.foot/]
