[#ftl]
[@b.head/]
[@b.grid items=technics var="technic"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="代码"]${technic.code}[/@]
    [@b.col width="15%" property="name" title="名称"][@b.a href="!info?id=${technic.id}"]${technic.name}[/@][/@]
    [@b.col width="15%" property="depart.name" title="负责部门"/]
    [@b.col width="35%" property="description" title="说明"/]
    [@b.col width="20%" property="machine.name" title="加工中心"/]
  [/@]
[/@]
[@b.foot/]
