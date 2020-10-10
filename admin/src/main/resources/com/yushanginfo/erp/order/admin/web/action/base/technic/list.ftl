[#ftl]
[@b.head/]
[@b.grid items=technics var="technic"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="代码"]${technic.code}[/@]
    [@b.col width="20%" property="name" title="名称"][@b.a href="!info?id=${technic.id}"]${technic.name}[/@][/@]
  [/@]
[/@]
[@b.foot/]
