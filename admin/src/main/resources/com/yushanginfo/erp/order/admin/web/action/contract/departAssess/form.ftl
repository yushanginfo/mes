[#ftl]
[@b.head/]
[@b.toolbar title="订单评审"]bar.addBack();[/@]
  [@b.form action=b.rest.save(departAssess) theme="list"]
    <input type="hidden" name="salesOrderId" value="${salesOrder.id}"/>
    <input type="hidden" name="technicId" value="${technic.id}"/>
    [@b.field label="订单编号"]${salesOrder.code}[/@]
    [@b.field label="生产批号"]${salesOrder.batchNum}[/@]
    [@b.field label="产品图号"]${salesOrder.product.code}[/@]
    [@b.field label="订单数量"]${salesOrder.amount}[/@]
    [@b.field label="计划交付日期"]${salesOrder.requireOn?string("yyyy-MM-dd")}[/@]
    [@b.field label="订单备注"]${(salesOrder.remark)?default("无")}[/@]
    [@b.field label="工艺"]${technic.name}[/@]
    [@b.textfield name="departAssess.days" label="需要天数" value="${departAssess.days!}" required="true" /]
    [@b.select name="departAssess.factory.id" label="生产厂区" value="${(departAssess.factory.id)!}" required="true"  style="width:200px;" items=factories empty="..." option="id,name"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
