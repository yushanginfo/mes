[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.gridbar]
    bar.addItem("打回复审",action.single('review'));
    bar.addItem("直接通过",action.single('accept',"接受评审结果，结束复审?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="8%" property="batchNum" title="工单单号"]
      [@b.a href="!info?id=${workOrder.id}" title="${workOrder.orderType.code} ${workOrder.orderType.name}"]${workOrder.batchNum}[/@]
    [/@]
    [@b.col width="17%" property="product.code" title="产品图号"]${workOrder.product.specification!}[/@]
    [@b.col width="8%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yy-MM-dd"))!}[/@]
    [@b.col width="8%" property="createdAt" title="开单日期"]${(workOrder.createdAt?string("yy-MM-dd"))!}[/@]
    [@b.col width="8%" property="scheduledOn" title="评审交期"]${(workOrder.scheduledOn?string("yy-MM-dd"))!}[/@]
    [@b.col width="6%" property="amount" title="数量"/]
    [@b.col width="8%" property="materialDate" title="到料日期"]
      [#if workOrder.materialAssess??][#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yy-MM-dd"))!}[/#if][/#if]
    [/@]
    [@b.col width="20%" title="生产周期评估"]
      [#assign assessMap={}]
      [#list workOrder.technics as wt]
        [#if wt.technic.assessGroup?? && wt.days??]
        [#assign assessGroupId=wt.technic.assessGroup.id?string/]
        [#assign assessMap=assessMap+{assessGroupId:(wt.days + (assessMap[assessGroupId]!0))}]
        [/#if]
      [/#list]
      [#assign assessGroups= []/]
      [#list workOrder.technics as wt]
        [#if wt.technic.assessGroup?? && !assessGroups?seq_contains(wt.technic.assessGroup)]
           [#assign assessGroups= assessGroups+[wt.technic.assessGroup]/]
        [/#if]
      [/#list]
      <div class="btn-group btn-group-sm" role="group">
      [#list assessGroups as assessGroup]
       [#if assessMap[assessGroup.id?string]??]
         ${assessGroup.name} ${assessMap[assessGroup.id?string]}
       [#else]
         ${assessGroup.name} --
       [/#if]
      [/#list]
     </div>
    [/@]
    [@b.col width="7%" property="saler.name" title="业务员"/]
    [@b.col width="7%" property="assessStatus" title="评审状态"]
      ${workOrder.assessStatus.name} [#if workOrder.assessStatus.name=="复审中"]${workOrder.reviewEvents?size}[/#if]
    [/@]
  [/@]
[/@]
<script>
  function cancel(){
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
