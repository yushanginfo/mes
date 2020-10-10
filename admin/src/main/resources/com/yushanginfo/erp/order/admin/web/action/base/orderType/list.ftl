[#ftl]
[@b.head/]
[@b.grid items=orderTypes var="orderType"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="代码"]${orderType.code}[/@]
    [@b.col width="20%" property="name" title="名称"][@b.a href="!info?id=${orderType.id}"]${orderType.name}[/@][/@]
  [/@]
[/@]
[@b.foot/]
