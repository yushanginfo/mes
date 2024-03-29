[#ftl]
[@b.head/]
[@b.toolbar title="修改用户信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(user) theme="list"]
    [@b.textfield name="user.code" label="工号" value="${user.code!}" required="true" maxlength="30"/]
    [@b.textfield name="user.name" label="姓名" value="${user.name!}" required="true" maxlength="80"/]
    [@b.select name="user.department.id" label="所在部门" value="${(user.department.id)!}" required="true"
               style="width:200px;" items=departments empty="..."/]
    [@b.select name="user.factory.id" label="所在厂区" value="${(user.factory.id)!}" required="false"
               style="width:200px;" items=factories empty="..."/]
    [@b.textfield name="user.mobile" label="电话" value="${user.mobile!}" maxlength="15"/]
    [@b.textfield name="user.email" label="邮箱" value="${user.email!}"  maxlength="80"/]
    [@b.startend name="user.beginOn,user.endOn"  label="有效期限" start=user.beginOn end=user.endOn! required="true,false"/]
    [@b.textfield name="user.remark" label="说明" value="${user.remark!}" maxlength="190"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
