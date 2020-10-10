[#ftl]
[@b.head/]
[@b.grid items=salesOrders var="salesOrder"]
  [@b.gridbar]
    bar.addItem("打回复审",action.multi('review'));
    bar.addItem("取消订单",'cancel()');
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="订单编号"][@b.a href="!info?id=${salesOrder.id}"]${salesOrder.code}[/@][/@]
    [@b.col width="10%" property="batchNum" title="生产批号"/]
    [@b.col width="10%" property="orderType.name" title="订单类型"/]
    [@b.col width="10%" property="product.customer.code" title="顾客代码"/]
    [@b.col width="10%" property="product.code" title="产品图号"/]
    [@b.col width="10%" property="count" title="订单数量"/]
    [@b.col width="10%" property="requireOn" title="计划交付日期"]${(salesOrder.requireOn?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="10%" property="materialDate" title="到料日期"]${(salesOrder.materialDate?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="10%" property="" title="部门评审（天数/厂区）"]
      [#list salesOrder.product.technics! as technic]
        ${(technic.name)!}[#if departAssessMap.get(salesOrder)?? && departAssessMap.get(salesOrder).get(technic)??](${departAssessMap.get(salesOrder).get(technic).days}天/${departAssessMap.get(salesOrder).get(technic).factory.name})[/#if]
        [#if technic_has_next]<br>[/#if]
      [/#list]
    [/@]
    [@b.col width="10%" property="scheduledOn" title="计划完工日期"]${(salesOrder.scheduledOn?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="10%" property="status.name" title="订单状态"/]
  [/@]
[/@]
<script>
  function cancel()
  {
    var salesOrderIds = bg.input.getCheckBoxValues("salesOrder.id");
    if(salesOrderIds=="" || salesOrderIds==null)
      window.alert('你没有选择要操作的记录！');
    else {
      var remark=prompt("请填写取消备注","");
      if(remark)
      {
        bg.form.submit("salesOrderSearchForm","${b.url('!cancel')}"+"?salesOrder.id=" + salesOrderIds +"&remark=" +remark);
      }
    }
  }
</script>
[@b.foot/]
