[#ftl]
[@b.head/]
[@b.grid items=technics var="technic"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    var menu = bar.addMenu("更改评审组为...");
    [#list groups as group]
      menu.addItem("${group.name}",action.multi("batchUpdateGroup","确定更新?","&group.id=${group.id}"));
    [/#list]
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="代码"]${technic.code}[/@]
    [@b.col width="15%" property="name" title="名称"][@b.a href="!info?id=${technic.id}"]${technic.name}[/@][/@]
    [@b.col width="15%" property="assessGroup.id" title="评审组"]${(technic.assessGroup.name)!'--'}[/@]
    [@b.col width="8%" property="internal" title="性质"]
      [#if technic.internal]厂内[#else]委外[/#if]
    [/@]
    [@b.col width="17%" property="machine.name" title="加工中心"/]
    [@b.col property="description" title="说明"/]
  [/@]
[/@]
[@b.foot/]
