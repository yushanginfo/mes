[#ftl]
[@b.head/]
[@b.toolbar title="订单类型"]bar.addBack();[/@]
  [@b.form action=b.rest.save(salesOrderType) theme="list"]
    [@b.textfield name="salesOrderType.code" label="代码" value="${salesOrderType.code!}" required="true" maxlength="10"/]
    [@b.textfield name="salesOrderType.name" label="名称" value="${salesOrderType.name!}" required="true" maxlength="80"/]
    [@b.textfield name="salesOrderType.remark" label="备注" value="${salesOrderType.remark!}" maxlength="190"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
