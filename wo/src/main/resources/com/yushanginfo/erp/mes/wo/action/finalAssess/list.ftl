[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.gridbar]
    bar.addItem("打回复审",action.multi('review'));
    bar.addItem("取消工单",'cancel()');
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="5%" property="customer.code" title="顾客"]${workOrder.customer.code}[/@]
    [@b.col width="8%" property="code" title="工单编号"][@b.a href="!info?id=${workOrder.id}"]${workOrder.code}[/@][/@]
    [@b.col width="10%" property="product.code" title="产品图号"]${workOrder.product.specification!}[/@]
    [@b.col width="5%" property="orderType.name" title="工单类型"/]
    [@b.col width="8%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]

    [@b.col width="8%" property="plannedEndOn" title="计划交期"/]
    [@b.col width="8%" property="batchNum" title="生产批号"/]
    [@b.col width="5%" property="amount" title="计划数量"/]

    [@b.col width="7%" property="materialDate" title="到料日期"]${(workOrder.materialDate?string("MM-dd"))!}[/@]
    [@b.col width="20%" title="生产周期评估"]
      [#assign assessMap={}]
      [#list workOrder.assesses as assess]
        [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
      [/#list]
      <div class="btn-group btn-group-sm"   role="group">
      [#list workOrder.technicScheme.technics as pt]
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
    [@b.col width="6%" property="status.name" title="工单状态"/]
  [/@]
[/@]
<script>
  function cancel()
  {
    var workOrderIds = bg.input.getCheckBoxValues("workOrder.id");
    if(workOrderIds=="" || workOrderIds==null)
      window.alert('你没有选择要操作的记录！');
    else {
      var remark=prompt("请填写取消备注","");
      if(remark)
      {
        bg.form.submit("workOrderSearchForm","${b.url('!cancel')}"+"?workOrder.id=" + workOrderIds +"&remark=" +remark);
      }
    }
  }
</script>
[@b.foot/]
