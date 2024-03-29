[#ftl]
[@b.head/]
[@b.toolbar title="工单评审"]bar.addBack();[/@]
  [@b.form action=b.rest.save(departAssess) theme="list"]
    <input type="hidden" name="workOrderId" value="${workOrder.id}"/>
    <input type="hidden" name="technicId" value="${technic.id}"/>
    [@b.field label="工单单号"]${workOrder.orderType.code}-${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.code}[/@]
    [@b.field label="工单数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]
    [@b.field label="工单备注"]${(workOrder.remark)?default("无")}[/@]
    [@b.field label="工艺"]${technic.name}[/@]
    [@b.textfield name="departAssess.days" label="需要天数" value="${departAssess.days!}" required="true" /]
    [@b.select name="departAssess.factory.id" label="生产厂区" value="${(departAssess.factory.id)!}" required="true"  style="width:200px;" items=factories empty="..." option="id,name"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
