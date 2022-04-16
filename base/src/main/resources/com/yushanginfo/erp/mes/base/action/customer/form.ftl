[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
[@b.toolbar title="修改客户信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(customer) theme="list"]
    [@b.textfield name="customer.code" label="客户编号" value="${customer.code!}" required="true" maxlength="30"/]
    [@b.textfield name="customer.name" label="客户名称" value="${customer.name!}" required="true" maxlength="80"/]
    [@b.select label="业务人员" name="customer.saler.id"  items=users  value=customer.saler! option=r"${item.code}${item.name}" required="false" /]
    [@b.select label="总公司" name="customer.parent.id"  items=parents  option=r"${item.code}${item.name}" required="false" /]
    [@b.textfield name="customer.quickCode" label="快捷码" value="${customer.quickCode!}" maxlength="5"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
<div style="margin:200px 0px 0px 0px"></div>
[@b.foot/]
