[#ftl]
[@b.head/]
[@b.grid items=assessMembers var="assessMember"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入","importForm()");
  [/@]
  [@b.row]
    [@b.boxcol name="assessMember.id"/]
    [@b.col width="10%" property="user.code" title="工号"]${assessMember.user.code}[/@]
    [@b.col width="30%" property="user.name" title="姓名"/]
    [@b.col width="30%" property="group.name" title="评审组"]${(assessMember.group.name)!}[/@]
    [@b.col title="厂区"]${assessMember.factory.name}[/@]
  [/@]
[/@]

<script>
  function importForm(){
    bg.form.submit(document.indexForm,"${b.url('!importForm')}","_blank")
  }
</script>

[@b.foot/]
