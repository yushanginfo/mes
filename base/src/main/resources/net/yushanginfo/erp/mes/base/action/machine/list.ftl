[#ftl]
[@b.head/]
[@b.grid items=machines var="machine"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="30%" property="code" title="代码"]${machine.code}[/@]
    [@b.col width="65%" property="name" title="名称"][@b.a href="!info?id=${machine.id}"]${machine.name}[/@][/@]
  [/@]
[/@]
[@b.foot/]
