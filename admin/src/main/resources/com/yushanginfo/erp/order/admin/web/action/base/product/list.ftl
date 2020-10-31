[#ftl]
[@b.head/]
[@b.grid items=products var="product"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="code" title="品号"/]
    [@b.col width="15%" property="name" title="品名"][@b.a href="!info?id=${product.id}"]${product.name}[/@][/@]
    [@b.col width="20%" title="材料"]

    [/@]
    [@b.col width="20%" title="规格"]

    [/@]
    [@b.col width="10%" property="unit.name"  title="计量单位"/]
    [@b.col width="15%"   title="工艺列表"]
      [#list product.technicSchemes as scheme]
        ${(scheme.name)!}[#if scheme_has_next],[/#if]
      [/#list]
    [/@]
  [/@]
[/@]
[@b.foot/]
