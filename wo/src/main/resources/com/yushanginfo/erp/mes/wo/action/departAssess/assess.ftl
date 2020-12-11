[#ftl]
[@b.head/]
  [#assign assessMap={}]
  [#list workOrder.assesses as assess]
    [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
  [/#list]

[@b.toolbar title=assessGroup.name+  " 工单评审"]bar.addBack();[/@]
  [@b.form action="!saveAssess" theme="list"]
    [@b.field label="工单单号"]${workOrder.orderType.name}-${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]
    [@b.field label="计划交期"]${(workOrder.plannedEndOn?string("yyyy-MM-dd"))}[/@]
    [@b.field label="到料日期"][#if workOrder.materialAssess??] [#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yyyy-MM-dd"))!}[/#if][/#if][/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]

    [#list workOrder.technicScheme.technics as pt]
    [#if !pt.technic.assessGroup??][#continue/][/#if]
    [#if assessGroup.id==pt.technic.assessGroup.id]
    [#assign technic=pt.technic/]
    [@b.field label=pt.technic.name]
        <select name="technic_${technic.id}.factory.id" style="width:80px">
          [#if (assessMap[technic.id?string].factory)??]
            [#assign assessFactory = assessMap[technic.id?string].factory/]
          [#else]
            [#assign assessFactory= workOrder.factory/]
          [/#if]
          [#list factories as f]
            <option value="${f.id}" [#if f.id==assessFactory.id]selected="selected"[/#if]>${f.name}</option>
          [/#list]
        </select>
        <input name="technic_${technic.id}.days" style="width:80px" type="number" value="${(assessMap[technic.id?string].days)!}"/>天
        <label class="comment">${pt.description!}
               [#if pt.machine??]${pt.machine.code} ${pt.machine.name}
               [#elseif pt.supplier??] ${pt.supplier.code} ${pt.supplier.name}
               [/#if]
        </label>
    [/@]
    [/#if]
    [/#list]
    [@b.formfoot]
    <input type="hidden" name="workOrderId" value="${workOrder.id}"/>
    <input type="hidden" name="assessGroupId" value="${assessGroup.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
  <script>
     function checkDays(form){

     }
  </script>
[@b.foot/]
