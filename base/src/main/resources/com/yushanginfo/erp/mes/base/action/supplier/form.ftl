[#ftl]
[@b.head/]
[@b.toolbar title="供应商"]bar.addBack();[/@]
  [@b.form action=b.rest.save(supplier) theme="list"]
    [@b.textfield name="supplier.code" label="代码" value="${supplier.code!}" required="true" maxlength="10"/]
    [@b.textfield name="supplier.name" label="名称" value="${supplier.name!}" required="true" maxlength="80"/]
    [@b.textfield name="supplier.remark" label="备注" value="${supplier.remark!}" maxlength="190"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
