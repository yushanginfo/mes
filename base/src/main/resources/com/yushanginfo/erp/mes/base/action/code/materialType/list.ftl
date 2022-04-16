[#ftl]
[@b.head/]
[@b.grid items=materialTypes var="materialType"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="30%" property="code" title="代码"]${materialType.code}[/@]
    [@b.col width="65%" property="name" title="名称"][@b.a href="!info?id=${materialType.id}"]${materialType.name}[/@][/@]
  [/@]
[/@]
[@b.foot/]
