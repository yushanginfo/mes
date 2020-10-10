[#ftl]
[@b.head/]
[@b.grid items=users var="user"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("${b.text('action.export')}",action.exportData("code:工号,name:姓名,department.name:部门,mobile:移动电话,email:电子邮箱",null,'fileName=用户信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="20%" property="code" title="工号"]${user.code}[/@]
    [@b.col width="20%" property="name" title="姓名"][@b.a href="!info?id=${user.id}"]${user.name}[/@][/@]
    [@b.col width="20%" property="department" title="所在部门"]${(user.department.name)!}[/@]
    [@b.col width="20%" property="mobile" title="移动电话"/]
    [@b.col width="20%" property="email" title="电子邮箱"/]
    [@b.col width="9%" property="endOn" title="有效期限"]${(user.endOn?string('yy-MM-dd'))!'--'}[/@]
  [/@]
[/@]
[@b.foot/]
