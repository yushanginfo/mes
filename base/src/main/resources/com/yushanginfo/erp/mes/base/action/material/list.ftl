[#ftl]
[@b.head/]
[@b.grid items=materials var="material"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="20%" property="code" title="品号"/]
    [@b.col width="20%" property="name" title="品名"][@b.a href="!info?id=${material.id}"]${material.name}[/@][/@]
    [@b.col width="20%" property="specification" title="规格"/]
    [@b.col width="20%" property="materialType.name"  title="品号类别"/]
    [@b.col width="15%" property="unit.name"  title="计量单位"/]
  [/@]
[/@]
[@b.foot/]
