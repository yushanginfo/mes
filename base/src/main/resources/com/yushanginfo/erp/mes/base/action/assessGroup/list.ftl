[#ftl]
[@b.head/]
[@b.grid items=assessGroups var="assessGroup"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol name="assessGroup.id"/]
    [@b.col width="10%" property="code" title="代码"]${assessGroup.code}[/@]
    [@b.col width="15%" property="name" title="名称"/]
    [@b.col width="20%" property="director" title="负责人"]${(assessGroup.director.code)!} ${(assessGroup.director.name)!}[/@]
    [@b.col width="30%" title="成员"]
      [#list assessGroup.members as m] ${m.user.code} ${m.user.name} ${m.factory.name}[#if m_has_next],[/#if] [/#list]
    [/@]
  [/@]
[/@]
[@b.foot/]
