[#ftl]
[@b.head/]
[@b.grid items=reviewers var="reviewer"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol name="reviewer.id"/]
    [@b.col width="10%" property="user.code" title="工号"]${reviewer.user.code}[/@]
    [@b.col width="35%" property="user.name" title="姓名"/]
    [@b.col width="20%" title="厂区"][#list reviewer.factories as f]${f.name}[#if f_has_next],[/#if][/#list][/@]
    [@b.col width="10%" title="复审轮次"][#list reviewer.rounds as f]${f}[#if f_has_next],[/#if][/#list][/@]
  [/@]
[/@]
[@b.foot/]
