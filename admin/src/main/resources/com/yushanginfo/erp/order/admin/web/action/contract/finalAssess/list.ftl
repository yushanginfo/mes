[#ftl]
[@b.head/]
[@b.grid items=salesOrders var="salesOrder"]
  [@b.gridbar]
    bar.addItem("打回复审",action.multi('review'));
    bar.addItem("取消订单",'cancel()');
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="5%" property="customer.code" title="顾客"]${salesOrder.customer.code}[/@]
    [@b.col width="8%" property="code" title="订单编号"][@b.a href="!info?id=${salesOrder.id}"]${salesOrder.code}[/@][/@]
    [@b.col width="10%" property="product.code" title="产品图号"]${salesOrder.product.specification!}[/@]
    [@b.col width="5%" property="orderType.name" title="订单类型"/]
    [@b.col width="8%" property="requireOn" title="交期"]${(salesOrder.requireOn?string("yyyy-MM-dd"))!}[/@]

    [@b.col width="8%" property="scheduledOn" title="计划交期"/]
    [@b.col width="8%" property="batchNum" title="生产批号"/]
    [@b.col width="5%" property="amount" title="计划数量"/]

    [@b.col width="7%" property="materialDate" title="到料日期"]${(salesOrder.materialDate?string("MM-dd"))!}[/@]
    [@b.col width="20%" title="生产周期评估"]
      [#assign assessMap={}]
      [#list salesOrder.assesses as assess]
        [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
      [/#list]
      <div class="btn-group btn-group-sm"   role="group">
      [#list salesOrder.technicScheme.technics as pt]
        [#if assessMap[pt.technic.id?string]??]
          [#assign assess =assessMap[pt.technic.id?string]/]
          [#if assess.passed]
          <a href="#" class="btn btn-info" style="font-size:0.8em" title="${pt.technic.name} ${assess.days}天 ${assess.factory.name}">${pt.technic.name} ${assess.days}</a>
          [#else]
          [@b.a class="btn btn-primary" style="font-size:0.8em"]${pt.technic.name} ${assess.days}[/@]
          [/#if]
        [#else]
          [@b.a class="btn btn-primary" style="font-size:0.8em"]${pt.technic.name}[/@]
        [/#if]
      [/#list]
     </div>
    [/@]
    [@b.col width="6%" property="status.name" title="订单状态"/]
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
