[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="8%" property="batchNum" title="工单单号"]
       [@b.a href="!info?id=${workOrder.id}" title="${workOrder.orderType.code} ${workOrder.orderType.name}"]${workOrder.batchNum}[/@]
    [/@]
    [@b.col width="15%" property="product.code" title="产品图号"]${workOrder.product.specification!}[/@]
    [@b.col width="8%" property="deadline" title="客户交期"]<span title="开单日期:${(workOrder.createdAt?string("yy-MM-dd"))!}">${(workOrder.deadline?string("yy-MM-dd"))!}</span>[/@]
    [@b.col width="5%" property="amount" title="数量"/]
    [@b.col width="8%" property="assessBeginAt" title="评审开始"]${((workOrder.assessBeginAt)?string("yy-MM-dd"))!}[/@]

    [@b.col width="8%" property="materialDate" title="到料日期"]
      [#if workOrder.materialAssess??][#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yy-MM-dd"))!}[/#if][/#if]
    [/@]
    [@b.col width="29%" title="生产周期评估"]
      [#assign assessMap={}]
      [#list workOrder.technics as wt]
        [#if wt.technic.assessGroup?? && wt.days??]
        [#assign assessGroupId=wt.technic.assessGroup.id?string/]
        [#assign assessMap=assessMap+{assessGroupId:(wt.days + (assessMap[assessGroupId]!0))}]
        [/#if]
      [/#list]
      [#assign assessGroups= []/]
      [#assign cannotAssessed= []/]
      [#list workOrder.technics as wt]
        [#if wt.technic.assessGroup??]
          [#if !assessGroups?seq_contains(wt.technic.assessGroup)]
             [#assign assessGroups = assessGroups+[wt.technic.assessGroup]/]
          [/#if]
        [#else]
           [#assign cannotAssessed = cannotAssessed + [wt.technic]/]
        [/#if]
      [/#list]
    [#if workOrder.canAssess && cannotAssessed?size=0][#--所有工艺均设置了评审组，具备评审条件--]
      [#assign btnClz][#if workOrder.inReview]btn btn-warning[#else]btn btn-info[/#if][/#assign]
      <div class="btn-group btn-group-sm" role="group">
      [#list assessGroups?sort_by("code") as assessGroup]
       [#if assessMap[assessGroup.id?string]??]
         [#if myGroups?seq_contains(assessGroup)]
           <button class="${btnClz}" style="font-size:0.8em" onclick="assess('${workOrder.id}','${assessGroup.id}')">${assessGroup.name} ${assessMap[assessGroup.id?string]}</button>
         [#else]
           <button class="${btnClz}" style="font-size:0.8em">${assessGroup.name} ${assessMap[assessGroup.id?string]}</button>
         [/#if]
       [#else]
         [#if myGroups?seq_contains(assessGroup)]
           <button class="btn btn-primary" style="font-size:0.8em" onclick="assess('${workOrder.id}','${assessGroup.id}')">${assessGroup.name}</button>
         [#else]
           <button class="btn btn-primary" style="font-size:0.8em">${assessGroup.name}</button>
         [/#if]
       [/#if]
      [/#list]
     </div>
   [#else]
      [#if cannotAssessed?size>0 ]
         缺少评审设置:[#list cannotAssessed as t ]${t.name}&nbsp;[/#list]
      [#else]
        [#assign btnClz][#if workOrder.inReview]btn btn-warning[#else]btn btn-success[/#if][/#assign]
        <div class="btn-group btn-group-sm" role="group">
        [#list assessGroups?sort_by("code") as assessGroup]
         [#if assessMap[assessGroup.id?string]??]
           <button class="${btnClz}" style="font-size:0.8em">${assessGroup.name} ${assessMap[assessGroup.id?string]}</button>
         [#else]
           <button class="btn btn-primary" style="font-size:0.8em">${assessGroup.name}</button>
         [/#if]
        [/#list]
       </div>
      [/#if]
   [/#if]
    [/@]
    [@b.col width="8%" property="status.name" title="工单状态"/]
    [@b.col width="7%" property="assessStatus" title="评审状态"]${workOrder.assessStatus.name} [#if workOrder.assessStatus.name=="复审中"]${workOrder.reviewEvents?size}[/#if][/@]
  [/@]
[/@]
[@b.form name="assessForm" action="!assess"]
  <input type="hidden" name="_params" value="${b.paramstring}"/>
[/@]
<script>
   function assess(workOrderId,assessGroupId){
      bg.form.submit(document.assessForm,"${base}/depart-assess/assess?workOrderId="+workOrderId+"&assessGroupId="+assessGroupId);
   }
</script>
[@b.foot/]
