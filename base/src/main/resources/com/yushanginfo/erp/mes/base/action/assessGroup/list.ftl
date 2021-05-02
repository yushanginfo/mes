[#ftl]
[@b.head/]
[@b.grid items=assessGroups var="assessGroup"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入","importForm()");
  [/@]
  [@b.row]
    [@b.boxcol name="assessGroup.id"/]
    [@b.col width="6%" property="code" title="代码"]${assessGroup.code}[/@]
    [@b.col width="8%" property="name" title="名称"/]
    [@b.col width="10%" property="department.name" title="部门"/]
    [@b.col width="8%" property="director" title="负责人"]${(assessGroup.director.code)!} ${(assessGroup.director.name)!}[/@]
    [@b.col width="58%" title="成员"]
      [#assign f_members ={}/]
      [#list assessGroup.members as m]
         [#if !f_members[m.factory.name]?exists]
            [#assign f_members = f_members + {m.factory.name:[]}/]
         [/#if]
         [#assign f_members =f_members+{m.factory.name:f_members[m.factory.name]+[m]}/]
      [/#list]
      [#list f_members as factoryName,members]
        ${factoryName}[#list members?sort_by(["user","code"]) as m] <span title="${m.user.code}">${m.user.name} </span>[/#list]<br>
      [/#list]

    [/@]
  [/@]
[/@]
<script>
  function importForm(){
    bg.form.submit(document.indexForm,"${b.url('!importForm')}","_blank")
  }
</script>
[@b.foot/]
