[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="8%" property="batchNum" title="工单单号"]
       [@b.a href="work-order!info?id=${workOrder.id}" title="${workOrder.orderType.code} ${workOrder.orderType.name}"]${workOrder.batchNum}[/@]
    [/@]
    [@b.col width="15%" property="product.code" title="产品图号"]${workOrder.product.specification!}[/@]
    [@b.col width="8%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yy-MM-dd"))!}[/@]
    [@b.col width="8%" property="plannedEndOn" title="计划交期"]${(workOrder.plannedEndOn?string("yy-MM-dd"))!}[/@]
    [@b.col width="5%" property="amount" title="数量"/]

    [@b.col width="8%" property="materialDate" title="到料日期"]
      [#if workOrder.materialAssess??][#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yy-MM-dd"))!}[/#if][/#if]
    [/@]
    [@b.col width="29%" title="生产周期评估"]
      [#assign assessMap={}]
      [#list workOrder.assesses as assess]
        [#if assess.technic.assessGroup??]
        [#assign assessGroupId=assess.technic.assessGroup.id?string/]
        [#assign assessMap=assessMap+{assessGroupId:(assess.days + (assessMap[assessGroupId]!0))}]
        [/#if]
      [/#list]
      [#assign assessGroups= []/]
    [#if workOrder.technicScheme??]
      [#list workOrder.technicScheme.technics as pt]
        [#if pt.technic.assessGroup?? && !assessGroups?seq_contains(pt.technic.assessGroup)]
           [#assign assessGroups= assessGroups+[pt.technic.assessGroup]/]
        [/#if]
      [/#list]
    [/#if]
      <div class="btn-group btn-group-sm" role="group">
      [#list assessGroups as assessGroup]
       [#if assessMap[assessGroup.id?string]??]
         [@b.a class="btn btn-info" style="font-size:0.8em" href="!assess?workOrderId=${workOrder.id}&assessGroupId=${assessGroup.id}"]${assessGroup.name} ${assessMap[assessGroup.id?string]}[/@]
       [#else]
         [@b.a class="btn btn-primary" style="font-size:0.8em" href="!assess?workOrderId=${workOrder.id}&assessGroupId=${assessGroup.id}"]${assessGroup.name}[/@]
       [/#if]
      [/#list]
     </div>
    [/@]
    [@b.col width="8%" property="status.name" title="工单状态"/]
    [@b.col width="7%" property="assessStatus" title="评审状态"]${workOrder.assessStatus.name}[/@]
  [/@]
[/@]
[@b.foot/]
