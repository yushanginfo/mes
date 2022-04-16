[#ftl]
[@b.head/]

[@b.toolbar title=assessGroup.name+  " 工单评审"]bar.addBack();[/@]
  [@b.form action="!saveAssess" theme="list"]
    [@b.field label="工单单号"]${workOrder.orderType.name}-${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!'--'}[/@]
    [@b.field label="到料日期"]
      [#if workOrder.materialAssess??]
        [#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yyyy-MM-dd"))!}[/#if]
        <span style="font-size:0.8rem;color: #999;">${(workOrder.materialAssess.assessedBy.name)!} ${(workOrder.materialAssess.createdAt)?string("yyyy-MM-dd HH:mm")}</span>
      [#else]
        尚未填写到料信息
      [/#if]
    [/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]

    [#list workOrder.technics?sort_by("indexno") as wt]
    [#if !wt.technic.assessGroup??][#continue/][/#if]
    [#if assessGroup.id==wt.technic.assessGroup.id]
    [#assign technic=wt.technic/]
    [@b.field label=wt.indexno+" "+ wt.technic.name]
        <select name="${wt.id}.factory.id" style="width:100px">
          [#if (wt.factory)??]
            [#assign assessFactory = wt.factory/]
          [#else]
            [#assign assessFactory= workOrder.factory/]
          [/#if]
          [#list factories as f]
            <option value="${f.id}" [#if f.id==assessFactory.id]selected="selected"[/#if]>${f.name}</option>
          [/#list]
        </select>
        <input name="${wt.id}.days" style="width:80px" type="number" value="${wt.days!}"/>天
        <label class="comment">
           [#if wt.machine??]${wt.machine.code} ${wt.machine.name}
           [#elseif wt.supplier??] ${wt.supplier.code} ${wt.supplier.name}
           [/#if]
           &nbsp;${wt.description!}
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
