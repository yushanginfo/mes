[#ftl]
[@b.head/]
[@b.grid items=departments var="department"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="代码" width="10%"]${department.code}[/@]
    [@b.col property="name" title="名称"][@b.a href="!info?id=${department.id}"]${department.name}[/@][/@]
    [@b.col width="10%" property="indexno" title="序号" width="10%"]${department.indexno}[/@]
    [@b.col width="20%" property="parent.name" title="上级部门" width="35%"]${(department.parent.name)!}&nbsp;[/@]
  [/@]
[/@]
[@b.foot/]
