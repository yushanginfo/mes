[#ftl]
[@b.head/]
[@b.grid items=factories var="factory"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="编码"/]
    [@b.col width="20%" property="name" title="名称"][@b.a href="!info?id=${factory.id}"]${factory.name}[/@][/@]
    [@b.col width="50%" property="address" title="地址"/]
  [/@]
[/@]
[@b.foot/]
